package com.odos.odos_server.domain.challenge.resolver;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.challenge.dto.CreateChallengeInputDto;
import com.odos.odos_server.domain.challenge.service.ChallengeMutationService;
import com.odos.odos_server.domain.member.repository.MemberRepository;
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
  public ChallengeDto createChallenge(@Argument CreateChallengeInputDto input) {
    return challengeMutationService.createChallenge(input);
  }

  @MutationMapping
  public ChallengeDto applyMe(@Argument Long challengeId, @Argument List<String> goals) {
    return challengeMutationService.applyMe(challengeId, goals);
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
