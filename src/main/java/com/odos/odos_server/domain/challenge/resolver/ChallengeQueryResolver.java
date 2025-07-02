package com.odos.odos_server.domain.challenge.resolver;

import com.odos.odos_server.domain.challenge.dto.ChallengeConnectionDto;
import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.challenge.dto.ChallengeFilterInputDto;
import com.odos.odos_server.domain.challenge.service.ChallengeQueryService;
import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChallengeQueryResolver {

  private final ChallengeQueryService challengeQueryService;

  @QueryMapping
  public List<ChallengeDto> allChallenges() {
    return challengeQueryService.getAllChallenges();
  }

  @QueryMapping
  public List<ChallengeDto> myChallenges() {
    return challengeQueryService.getMyChallenges();
  }

  @QueryMapping
  public List<ChallengeDto> randomChallenges(@Argument int first) {
    return challengeQueryService.getRandomChallenges(first);
  }

  @QueryMapping
  public ChallengeDto challengeById(@Argument Long id) {
    return challengeQueryService.getChallengeById(id);
  }

  @QueryMapping
  public Boolean isChallengeLikedByMe(@Argument Long id) {
    return challengeQueryService.getIsChallengeLikedByMe(id);
  }

  @QueryMapping
  public MemberChallengeRole myChallengeApplicantStatus(@Argument Long id) {
    return challengeQueryService.getMyChallengeApplicantStatus(id);
  }

  @QueryMapping
  public ChallengeConnectionDto challengesList(
      @Argument ChallengeFilterInputDto filter,
      @Argument Optional<Integer> first,
      @Argument Optional<String> after) {
    return challengeQueryService.challengesList(filter, first.orElse(10), after.orElse(null));
  }

  /*
  @QueryMapping
  public ChallengeConnectionDto challengesList(
      @Argument ChallengeFilterInputDto filter, @Argument int first, @Argument String after) {
    return challengeQueryService.getChallengesWithFilter(filter, first, after);
  }

   */
}
