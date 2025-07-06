package com.odos.odos_server.domain.challenge.dto;

import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.common.Enum.ChallengeType;
import java.util.List;

public record CreateChallengeInputDto(
    String title,
    String description,
    String startDate,
    String endDate,
    int participantCount,
    ChallengeType goalType,
    List<String> goals,
    ChallengeCategory category
    // List<String> img
    ) {}
