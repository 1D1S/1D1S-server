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
import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

  private final Long currentMemberId = 1L;
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public ChallengeDto createChallenge(CreateChallengeInputDto input) {
    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Challenge challenge =
        Challenge.builder()
            .title(input.title())
            .category(input.category())
            .startDate(LocalDate.parse(input.startDate(), formatter))
            .endDate(LocalDate.parse(input.endDate(), formatter))
            .participantsCnt(input.participantCount())
            .type(input.goalType())
            .description(input.description())
            .hostMember(member)
            .build();

    challengeRepository.save(challenge);
    return ChallengeDto.from(challenge);
  }

  public ChallengeDto addApplicants(Long challengeId, List<String> goals) {
    Challenge challenge =
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    MemberChallenge memberChallenge =
        MemberChallenge.builder()
            .challenge(challenge)
            .member(member)
            .memberChallengeRole(MemberChallengeRole.REQUESTED)
            .build();
    memberChallengeRepository.save(memberChallenge);

    for (String g : goals) {
      ChallengeGoal goal =
          ChallengeGoal.builder().content(g).memberChallenge(memberChallenge).build();
      challengeGoalRepository.save(goal);
    }

    return ChallengeDto.from(challenge);
  }

  public ChallengeDto acceptApplicants(Long challengeId, List<Long> applicantIds) {
    for (Long memberId : applicantIds) {
      MemberChallenge mc =
          memberChallengeRepository
              .findByMemberIdAndChallengeId(memberId, challengeId)
              .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
      mc.setMemberChallengeRole(MemberChallengeRole.APPLICANT);
    }
    return ChallengeDto.from(challengeRepository.findById(challengeId).orElseThrow());
  }

  public ChallengeDto rejectApplicants(Long challengeId, List<Long> applicantIds) {
    for (Long memberId : applicantIds) {
      MemberChallenge mc =
          memberChallengeRepository
              .findByMemberIdAndChallengeId(memberId, challengeId)
              .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_APPLICANT_NOT_FOUND));
      mc.setMemberChallengeRole(MemberChallengeRole.REJECTED);
    }
    return ChallengeDto.from(
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND)));
  }

  public int addChallengeLike(Long challengeId) {
    Challenge challenge =
        challengeRepository
            .findById(challengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    ChallengeLike like = ChallengeLike.builder().challenge(challenge).member(member).build();
    challengeLikeRepository.save(like);
    return challengeLikeRepository.findByChallengeId(challengeId).size();
  }

  public int cancelChallengeLike(Long challengeId) {
    ChallengeLike like =
        challengeLikeRepository.findByChallengeIdAndMemberId(challengeId, currentMemberId);
    if (like != null) {
      challengeLikeRepository.delete(like);
    }
    return challengeLikeRepository.findByChallengeId(challengeId).size();
  }
}
