package com.odos.odos_server.domain.challenge.dto;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.common.Enum.ChallengeStatus;
import com.odos.odos_server.domain.common.Enum.ChallengeType;
import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import java.time.LocalDate;

public record ChallengeInfoDto(
    LocalDate startDate,
    LocalDate endDate,
    int participants,
    int maxParticipants,
    ChallengeCategory category,
    ChallengeType goalType,
    ChallengeStatus status) {

  public static ChallengeInfoDto from(Challenge entity) {
    int participants = 0;

    if (entity.getMemberChallenges() != null) {
      participants =
          (int)
              entity.getMemberChallenges().stream()
                  .filter(
                      mc ->
                          mc.getMemberChallengeRole() == MemberChallengeRole.HOST
                              || mc.getMemberChallengeRole() == MemberChallengeRole.APPLICANT)
                  .count();
    }

    ChallengeStatus status;
    LocalDate now = LocalDate.now();

    if (now.isBefore(entity.getStartDate())) {
      status = ChallengeStatus.RECRUITING;
    } else if (now.isAfter(entity.getEndDate())) {
      status = ChallengeStatus.COMPLETED;
    } else {
      status = ChallengeStatus.IN_PROGRESS;
    }

    return new ChallengeInfoDto(
        entity.getStartDate(),
        entity.getEndDate(),
        participants,
        entity.getParticipantsCnt(),
        entity.getCategory(),
        entity.getType(),
        status);
  }
}
