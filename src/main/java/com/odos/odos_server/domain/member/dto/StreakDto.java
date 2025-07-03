package com.odos.odos_server.domain.member.dto;

import java.util.List;

public record StreakDto(
    int todayGoalCount,
    int currentStreak,
    int totalDiaryCount,
    int totalGoalCount,
    int currentMonthDiaryCount,
    int currentMonthGoalCount,
    int maxStreak,
    List<DailyStreakDto> calendar) {}
