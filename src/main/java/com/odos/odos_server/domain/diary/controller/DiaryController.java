package com.odos.odos_server.domain.diary.controller;

import com.odos.odos_server.domain.diary.dto.DiaryCreate;
import com.odos.odos_server.domain.diary.entity.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DiaryController {

  private final DiaryService diaryService;

  @MutationMapping
  public Diary createDiary(@Argument Long memberId, @Argument DiaryCreate.CreateDiaryInput input) {
    return diaryService.createDiary(memberId, input);
  }
}
