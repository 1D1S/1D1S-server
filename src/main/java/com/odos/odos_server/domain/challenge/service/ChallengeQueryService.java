package com.odos.odos_server.domain.challenge.service;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.repository.ChallengeLikeRepository;
import com.odos.odos_server.domain.challenge.repository.ChallengeRepository;
import com.odos.odos_server.domain.challenge.repository.MemberChallengeRepository;
import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeQueryService {
    private final Long currentMemberId = 1L;

    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeLikeRepository challengeLikeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<ChallengeDto> getAllChallenges() {
        return challengeRepository.findAll().stream()
                .map(ChallengeDto::from)
                .toList();
    }

    public List<ChallengeDto> getMyChallenges() {
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return challengeRepository.findAll().stream()
                .filter(ch -> ch.getHostMember().getId().equals(member.getId()))
                .map(ChallengeDto::from)
                .toList();
    }
    public List<ChallengeDto> getRandomChallenges(int first) {
        List<Challenge> all = challengeRepository.findAll();
        Collections.shuffle(all);
        return all.stream().limit(first).map(ChallengeDto::from).toList();
    }

    public ChallengeDto getChallengeById(Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
        return ChallengeDto.from(challenge);
    }

    public boolean getIsChallengeLikedByMe(Long challengeId) {
        return challengeLikeRepository.existsByChallengeIdAndMemberId(challengeId, currentMemberId);
    }

    public MemberChallengeRole getMyChallengeApplicantStatus(Long challengeId) {
        return memberChallengeRepository.findByMemberIdAndChallengeId(currentMemberId, challengeId)
                .map(mc -> switch (mc.getMemberChallengeRole()) {
                    case HOST -> MemberChallengeRole.HOST;
                    case APPLICANT -> MemberChallengeRole.APPLICANT;
                    case REQUESTED -> MemberChallengeRole.REQUESTED;
                    case REJECTED -> MemberChallengeRole.REJECTED;
                    default -> MemberChallengeRole.NONE;
                })
                .orElse(MemberChallengeRole.NONE);
    }

}
