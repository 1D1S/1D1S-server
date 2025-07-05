package com.odos.odos_server.domain.common.dto;

import com.odos.odos_server.domain.challenge.entity.ChallengeGoal;

public record GoalDto(Long id, String Content) {
  public static GoalDto from(ChallengeGoal challengeGoal) {
    return new GoalDto(challengeGoal.getId(), challengeGoal.getContent());
  }
}
