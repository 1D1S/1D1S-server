package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.Enum.Feeling;
import com.odos.odos_server.domain.common.dto.DateDto;
import com.odos.odos_server.domain.diary.entity.Diary;
import java.time.LocalDateTime;
import java.util.List;

public record DiaryInfoDTO(
    LocalDateTime createdAt,
    DateDto date,
    Feeling feeling,
    List<DiaryGoalDTO> achievement,
    Integer achievementRate) {

  // DTO는 객체들간의 정보를 수합해서 내가 원하는 정보들과 타입으로 바꿔서 하나의 묶음으로 프론트에 보내주는 것.
  // 그래서 from이라는 메서드를 사용하고 빌드하는 것. 그 메서드를 DTO 안에 넣거나 converter를 씀
  public static DiaryInfoDTO from(Diary diary) {
    return null;
    // return new DiaryInfoDTO(
    // diary.getCreatedDate(),
    // DateDto
    // )
  }
}
