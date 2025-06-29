package com.odos.odos_server.domain.diary.controller;

import com.odos.odos_server.domain.diary.dto.CreateDiaryInput;
import com.odos.odos_server.domain.diary.dto.DiaryResponseDTO;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.service.DiaryService;
import com.odos.odos_server.error.exception.CustomException;
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
  public ResponseEntity<List<DiaryResponseDTO>> myDiaries(Long memberId) {
    List<DiaryResponseDTO> result = diaryService.getMyDiaries(memberId);
    return ResponseEntity.ok(result);
  }

  @MutationMapping
  public Boolean deleteDiary(@Argument Long diaryId) {
    try {
      diaryService.deleteDiary(diaryId);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @MutationMapping
  public Integer addDiaryLike(@Argument Long diaryId, @Argument Long memberId) {
    try {
      // memberId = CurrentUserContext.getCurrentMemberId();
      return diaryService.createDiaryLike(diaryId, memberId);
    } catch (CustomException e) {
      return 0;
    }
  }

  @MutationMapping
  public Integer cancelDiaryLike(@Argument Long diaryId, @Argument Long memberId) {
    // memberId = CurrentUserContext.getCurrentMemberId();
    return diaryService.cancelDiaryLike(diaryId, memberId);
  }

  @MutationMapping
  public Boolean isDiaryWrittenByMe(@Argument Long diaryId) {
    /* Long memberId = CurrentUserContext.getCurrentMemberId(); */
    Long memberId = 1L;
    return diaryService.isMine(diaryId, memberId);
  }

  @MutationMapping
  public Boolean isDiaryLikedByMe(Long diaryId) {
    /* Long memberId = CurrentUserContext.getCurrentMemberId(); */
    Long memberId = 1L;
    return diaryService.checkIfPressLikeByMe(diaryId, memberId);
  }

  @MutationMapping
  public ResponseEntity<List<DiaryResponseDTO>> randomDiaries(Integer first) {
    return null;
  }
}
