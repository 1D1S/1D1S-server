package com.odos.odos_server.domain.challenge.service;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.challenge.dto.CreateChallengeInputDto;
import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.repository.ChallengeRepository;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeMutationService {
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public ChallengeDto createChallenge(CreateChallengeInputDto input) {

        Member member =
                new Member(
                        null,
                        "test@example.com",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
        memberRepository.save(member);

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
}
