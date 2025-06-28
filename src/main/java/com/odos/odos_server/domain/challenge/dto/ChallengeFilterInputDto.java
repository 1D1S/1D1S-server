package com.odos.odos_server.domain.challenge.dto;

import com.odos.odos_server.domain.common.Enum.AgeGroup;
import com.odos.odos_server.domain.common.Enum.ChallengeStatus;
import com.odos.odos_server.domain.common.Enum.Job;
import com.odos.odos_server.domain.common.dto.DurationRangeDto;
import lombok.Getter;

@Getter
public record ChallengeFilterInputDto(
        String keyword, Job job, ChallengeStatus status, AgeGroup ageGroup, DurationRangeDto durationRangeDto
) {
}
