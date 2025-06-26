package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.Enum.Feeling;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryCreate {

  private Long challengeId; // 일지에 해당하는 챌린지 ID
  private String title; // 일지 제목
  private String content; // 일지 내용
  private Feeling feeling; // 작성 시 느낀 감정
  private Boolean isPublic; // 공개 여부
  private List<Long> goalIds; // 달성한 챌린지 목표 ID 목록
  private DateInput achievedDate; // 일지를 작성한(달성한) 날짜
  private List<String> images; // 이미지 URL 목록
}
