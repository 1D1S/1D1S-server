package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.Enum.Feeling;

public record CreateDiaryInput(
    // Long challengeId,
    String title,
    String content,
    Feeling feeling,
    Boolean isPublic,
    // List<Long> goalIds,
    String achievedDate
    // List<String> images
    ) {}
