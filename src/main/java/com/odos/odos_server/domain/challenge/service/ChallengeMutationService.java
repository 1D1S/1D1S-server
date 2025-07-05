package com.odos.odos_server.domain.challenge.service;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.challenge.dto.CreateChallengeInputDto;
import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.entity.ChallengeGoal;
import com.odos.odos_server.domain.challenge.entity.ChallengeLike;
import com.odos.odos_server.domain.challenge.entity.MemberChallenge;
import com.odos.odos_server.domain.challenge.repository.ChallengeGoalRepository;
import com.odos.odos_server.domain.challenge.repository.ChallengeLikeRepository;
import com.odos.odos_server.domain.challenge.repository.ChallengeRepository;
import com.odos.odos_server.domain.challenge.repository.MemberChallengeRepository;
import com.odos.odos_server.domain.common.Enum.ChallengeType;
import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import com.odos.odos_server.security.util.CurrentUserContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeMutationService {

  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
  private final MemberChallengeRepository memberChallengeRepository;
  private final ChallengeGoalRepository challengeGoalRepository;
  private final ChallengeLikeRepository challengeLikeRepository;

  public ChallengeDto createChallenge(CreateChallengeInputDto input) {
    Long currentMemberId = CurrentUserContext.getCurrentMemberId();
    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    LocalDate startDate;
    try {
      startDate = LocalDate.parse(input.startDate());
    } catch (DateTimeParseException e) {
      throw new CustomException(ErrorCode.INVALID_DATE_FORMAT);
    }

    LocalDate endDate = null;
    if (input.endDate() != null && !input.endDate().isBlank()) {
      try {
        endDate = LocalDate.parse(input.endDate());
      } catch (DateTimeParseException e) {
        throw new CustomException(ErrorCode.INVALID_DATE_FORMAT);
      }
    }
    Challenge challenge =
        Challenge.builder()
            .title(input.title())
            .category(input.category())
            .startDate(startDate)
            .endDate(endDate)
            .participantsCnt(input.participantCount())
            .type(input.goalType())
            .description(input.description())
            .hostMember(member)
            .build();

    challengeRepository.save(challenge);
    MemberChallenge memberChallenge =
        MemberChallenge.builder()
            .member(member)
            .challenge(challenge)
            .memberChallengeRole(MemberChallengeRole.HOST)
            .build();
    memberChallengeRepository.save(memberChallenge);
    for (String g : input.goals()) {
      ChallengeGoal challengeGoal =
          ChallengeGoal.builder().content(g).memberChallenge(memberChallenge).build();
      challengeGoalRepository.save(challengeGoal);
    }
    return ChallengeDto.from(challenge);
  }

  public ChallengeDto applyMe(Long challengeId, List<String> goals) {
    Long currentMemberId = CurrentUserContext.getCurrentMemberId();
    Challenge challenge =
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (memberChallengeRepository.existsByMemberAndChallenge(member, challenge)) {
      throw new CustomException(ErrorCode.ALREADY_APPLIED);
    }
    MemberChallenge memberChallenge =
        MemberChallenge.builder()
            .challenge(challenge)
            .member(member)
            .memberChallengeRole(MemberChallengeRole.REQUESTED)
            .build();
    memberChallengeRepository.save(memberChallenge);

    if (challenge.getType().equals(ChallengeType.FLEXIBLE)) {
      for (String g : goals) {
        ChallengeGoal goal =
            ChallengeGoal.builder().content(g).memberChallenge(memberChallenge).build();
        challengeGoalRepository.save(goal);
      }
    } else {
      MemberChallenge hostMemberChallenge =
          memberChallengeRepository
              .findByMemberIdAndChallengeId(challenge.getHostMember().getId(), challengeId)
              .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
      List<ChallengeGoal> challengeGoals =
          challengeGoalRepository.findByMemberChallenge(hostMemberChallenge);
      for (ChallengeGoal cg : challengeGoals) {

        challengeGoalRepository.save(
            ChallengeGoal.builder()
                .memberChallenge(memberChallenge)
                .content(cg.getContent())
                .build());
      }
    }

    return ChallengeDto.from(challenge);
  }

  public ChallengeDto acceptApplicants(Long challengeId, List<Long> applicantIds) {
    Challenge challenge =
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    Long currentMemberId = CurrentUserContext.getCurrentMemberId();
    if (!challenge.getHostMember().getId().equals(currentMemberId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }
    for (Long memberId : applicantIds) {
      MemberChallenge mc =
          memberChallengeRepository
              .findByMemberIdAndChallengeId(memberId, challengeId)
              .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
      if (mc.getMemberChallengeRole() != MemberChallengeRole.REQUESTED) {
        throw new CustomException(ErrorCode.INVALID_REQUEST);
      }
      mc.setMemberChallengeRole(MemberChallengeRole.APPLICANT);
    }
    return ChallengeDto.from(challenge);
  }

  public ChallengeDto rejectApplicants(Long challengeId, List<Long> applicantIds) {
    Challenge challenge =
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    Long currentMemberId = CurrentUserContext.getCurrentMemberId();
    if (!challenge.getHostMember().getId().equals(currentMemberId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }
    for (Long memberId : applicantIds) {
      MemberChallenge mc =
          memberChallengeRepository
              .findByMemberIdAndChallengeId(memberId, challengeId)
              .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_APPLICANT_NOT_FOUND));
      if (mc.getMemberChallengeRole() != MemberChallengeRole.REQUESTED) {
        throw new CustomException(ErrorCode.INVALID_REQUEST);
      }
      mc.setMemberChallengeRole(MemberChallengeRole.REJECTED);
    }
    return ChallengeDto.from(
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND)));
  }

  public int addChallengeLike(Long challengeId) {
    Long currentMemberId = CurrentUserContext.getCurrentMemberId();
    Challenge challenge =
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (challengeLikeRepository.existsByMemberAndChallenge(member, challenge)) {
      throw new CustomException(ErrorCode.ALREADY_LIKED);
    }
    ChallengeLike like = ChallengeLike.builder().challenge(challenge).member(member).build();
    challengeLikeRepository.save(like);
    return challengeLikeRepository.findByChallengeId(challengeId).size();
  }

  public int cancelChallengeLike(Long challengeId) {
    Long currentMemberId = CurrentUserContext.getCurrentMemberId();
    ChallengeLike like =
        challengeLikeRepository
            .findByChallengeIdAndMemberId(challengeId, currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_LIKE_NOT_FOUND));
    challengeLikeRepository.delete(like);
    return challengeLikeRepository.findByChallengeId(challengeId).size();
  }
}
