package com.odos.odos_server.domain.challenge.resolver;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.challenge.dto.CreateChallengeInputDto;
import com.odos.odos_server.domain.challenge.service.ChallengeMutationService;
import com.odos.odos_server.domain.common.Enum.Gender;
import com.odos.odos_server.domain.common.Enum.Job;
import com.odos.odos_server.domain.common.Enum.MemberRole;
import com.odos.odos_server.domain.common.Enum.SignupRoute;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChallengeMutationResolver {

  private final ChallengeMutationService challengeMutationService;
  private final MemberRepository memberRepository;

  @MutationMapping
  public Member createMember() {
    Member member =
        new Member(
            null,
            "test@example.com",
            SignupRoute.GOOGLE,
            "TestUser",
            "https://example.com/profile.png",
            Job.STUDENT,
            LocalDateTime.of(2000, 1, 1, 0, 0),
            Gender.FEMALE,
            true,
            MemberRole.USER,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

    return memberRepository.save(member);
  }

  @MutationMapping
  public ChallengeDto createChallenge(@Argument CreateChallengeInputDto input) {
    return challengeMutationService.createChallenge(input);
  }

  @MutationMapping
  public ChallengeDto addApplicants(@Argument Long challengeId, @Argument List<String> goals) {
    return challengeMutationService.addApplicants(challengeId, goals);
  }

  @MutationMapping
  public ChallengeDto acceptApplicants(
      @Argument Long challengeId, @Argument List<Long> applicants) {
    return challengeMutationService.acceptApplicants(challengeId, applicants);
  }

  @MutationMapping
  public ChallengeDto rejectApplicants(
      @Argument Long challengeId, @Argument List<Long> applicants) {
    return challengeMutationService.rejectApplicants(challengeId, applicants);
  }

  @MutationMapping
  public int addChallengeLike(@Argument Long challengeId) {
    return challengeMutationService.addChallengeLike(challengeId);
  }

  @MutationMapping
  public int cancelChallengeLike(@Argument Long challengeId) {
    return challengeMutationService.cancelChallengeLike(challengeId);
  }
}
