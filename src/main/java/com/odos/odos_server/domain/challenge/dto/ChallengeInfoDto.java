package com.odos.odos_server.domain.challenge.dto;

import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.common.Enum.ChallengeStatus;
import com.odos.odos_server.domain.common.Enum.ChallengeType;
import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import com.odos.odos_server.domain.challenge.entity.Challenge;
import java.time.LocalDateTime;

public record ChallengeInfoDto(
    LocalDateTime startDate,
    LocalDateTime endDate,
    int participants,
    Long maxParticipants,
    ChallengeCategory category,
    ChallengeType goalType,
    ChallengeStatus status) {
  public static ChallengeInfoDto from(Challenge entity) {
    int participants =
        (int)
            entity.getMemberChallenges().stream()
                .filter(
                    mc ->
                        mc.getMemberChallengeRole() == MemberChallengeRole.HOST
                            || mc.getMemberChallengeRole() == MemberChallengeRole.APPLICANT)
                .count();

    ChallengeStatus status;
    LocalDateTime now = LocalDateTime.now();
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
