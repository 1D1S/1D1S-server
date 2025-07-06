package com.odos.odos_server.domain.diary.dto;

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

  // DTO는 객체들간의 정보를 수합해서 내가 원하는 정보들과 타입으로 바꿔서 하나의 묶음으로 프론트에 보내주는 것.
  // 그래서 from이라는 메서드를 사용하고 빌드하는 것. 그 메서드를 DTO 안에 넣거나 converter를 씀
  public static DiaryInfoDto from(Diary diary) {
    String created = diary.getCreatedDate().toString();
    String diaryDate = diary.getDate().toString();

    List<DiaryGoalDto> goals =
        diary.getDiaryGoals() == null
            ? Collections.emptyList()
            : diary.getDiaryGoals().stream().map(DiaryGoalDto::from).toList();

    long achievedCount = goals.stream().filter(DiaryGoalDto::isAchieved).count();
    int achievementRate =
        goals.isEmpty() ? 0 : (int) Math.round((double) achievedCount * 100 / goals.size());

    return new DiaryInfoDto(created, diaryDate, diary.getFeeling(), goals, achievementRate);
  }
}
