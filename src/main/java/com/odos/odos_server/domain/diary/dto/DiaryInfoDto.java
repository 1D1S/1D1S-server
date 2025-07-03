package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.Enum.Feeling;
import com.odos.odos_server.domain.common.dto.DateDto;
import com.odos.odos_server.domain.diary.entity.Diary;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record DiaryInfoDto(
    DateDto createdAt,
    DateDto date,
    Feeling feeling,
    List<DiaryGoalDto> achievement,
    Integer achievementRate) {

  // DTO는 객체들간의 정보를 수합해서 내가 원하는 정보들과 타입으로 바꿔서 하나의 묶음으로 프론트에 보내주는 것.
  // 그래서 from이라는 메서드를 사용하고 빌드하는 것. 그 메서드를 DTO 안에 넣거나 converter를 씀
  public static DiaryInfoDto from(Diary diary) {
    DateDto created = toDateDto(diary.getCreatedDate());
    DateDto target = toDateDto(diary.getDate());

    List<DiaryGoalDto> goals =
        diary.getDiaryGoals() == null
            ? Collections.emptyList()
            : diary.getDiaryGoals().stream().map(DiaryGoalDto::from).toList();

    List<DiaryGoalDto> achievedGoals = new ArrayList<>();
    for (DiaryGoalDto d : goals) {
      if (d.isAchieved()) {
        achievedGoals.add(d);
      }
    }

    int achievementRate = 0;
    if (!goals.isEmpty()) {
      achievementRate = (int) Math.round((double) achievedGoals.size() * 100 / goals.size());
    }

    return new DiaryInfoDto(created, target, diary.getFeeling(), goals, achievementRate);
  }

  private static DateDto toDateDto(LocalDateTime time) {
    if (time == null) {
      return null;
    }
    return new DateDto(time.getYear(), time.getMonthValue(), time.getDayOfMonth());
  }
}
