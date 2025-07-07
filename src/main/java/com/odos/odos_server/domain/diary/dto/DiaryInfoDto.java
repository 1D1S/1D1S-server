package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.challenge.entity.ChallengeGoal;
import com.odos.odos_server.domain.common.Enum.Feeling;
import com.odos.odos_server.domain.diary.entity.Diary;
import java.util.Collections;
import java.util.List;

public record DiaryInfoDto(
    String createdAt,
    String date,
    Feeling feeling,
    List<DiaryGoalDto> achievement,
    Integer achievementRate) {

  public static DiaryInfoDto from(Diary diary) {
    String created = diary.getCreatedDate().toString();
    String diaryDate = diary.getDate().toString();

    List<DiaryGoalDto> goals =
        diary.getDiaryGoals() == null
            ? Collections.emptyList()
            : diary.getDiaryGoals().stream().map(DiaryGoalDto::from).toList();

    long achievedCount = goals.stream().filter(DiaryGoalDto::isAchieved).count();
    List<ChallengeGoal> allGoals =
        diary.getChallenge().getMemberChallenges().stream()
            .flatMap(mc -> mc.getChallengeGoals().stream())
            .toList();

    int totalChallengeGoals = allGoals.size();
    int achievementRate =
        goals.isEmpty()
            ? 0
            : (int) Math.round(((double) achievedCount / totalChallengeGoals) * 100);

    return new DiaryInfoDto(created, diaryDate, diary.getFeeling(), goals, achievementRate);
  }
}
