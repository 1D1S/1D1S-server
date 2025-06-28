package com.odos.odos_server.domain.diary.controller;

import com.odos.odos_server.domain.diary.dto.CreateDiaryInput;
import com.odos.odos_server.domain.diary.dto.DiaryResponseDTO;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.service.DiaryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<DiaryResponseDTO>> allDiaries() {

    List<DiaryResponseDTO> results = diaryService.getAllDiary();
    return ResponseEntity.ok(results);
  }

  @MutationMapping
  public ResponseEntity<DiaryResponseDTO> diaryById(@Argument Long diaryId) {
    DiaryResponseDTO result = diaryService.getDiaryById(diaryId);
    return ResponseEntity.ok(result);
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
