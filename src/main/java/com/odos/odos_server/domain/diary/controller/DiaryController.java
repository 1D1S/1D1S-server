package com.odos.odos_server.domain.diary.controller;

import com.odos.odos_server.domain.diary.dto.CreateDiaryInput;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.service.DiaryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DiaryController {

  private final DiaryService diaryService;

  @MutationMapping
  public Diary createDiary(@Argument Long memberId, @Argument CreateDiaryInput input) {
    return diaryService.createDiary(memberId, input);
  }

  @MutationMapping
  public Diary updateDiary(@Argument Long diaryId, @Argument CreateDiaryInput input) {
    return diaryService.updateDiary(diaryId, input);
  }

  @MutationMapping
  public List<Diary> allDiaries() {
    // return diaryService.getAllDiary();
    return null;
  }

  @MutationMapping
  public Diary diaryById(@Argument Long diaryId) {
    // return diaryService.getDiaryById(diaryId);
    return null;
  }

  @MutationMapping
  public List<Diary> isDiaryWrittenByMe(@Argument Long memberId) {
    // return diaryService.getMyDiaries(memberId);
    return null;
  }

  @MutationMapping
  public Boolean deleteDiary(@Argument Long diaryId) {
    return null;
  }
}
