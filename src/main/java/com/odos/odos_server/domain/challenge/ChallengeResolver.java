package com.odos.odos_server.domain.challenge;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.challenge.dto.CreateChallengeInputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChallengeResolver {
  private final ChallengeService challengeService;

  @QueryMapping
  public ChallengeDto challengeById(@Argument Long id) {
    return challengeService.getChallengeById(id);
  }

  @MutationMapping
  public ChallengeDto createChallenge(@Argument CreateChallengeInputDto input) {
    return challengeService.createChallenge(input);
  }
}
