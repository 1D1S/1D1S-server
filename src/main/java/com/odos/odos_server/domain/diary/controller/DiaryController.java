package com.odos.odos_server.domain.diary.controller;

import com.odos.odos_server.domain.diary.dto.CreateDiaryInput;
import com.odos.odos_server.domain.diary.dto.DiaryConnectionDTO;
import com.odos.odos_server.domain.diary.dto.DiaryReportDTO;
import com.odos.odos_server.domain.diary.dto.DiaryResponseDTO;
import com.odos.odos_server.domain.diary.service.DiaryService;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import com.odos.odos_server.security.util.CurrentUserContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DiaryController {

  private final DiaryService diaryService;

  @MutationMapping
  public ResponseEntity<DiaryResponseDTO> createDiary(@Argument CreateDiaryInput input) {
    try {
      Long memberId = CurrentUserContext.getCurrentMemberId();
      System.out.println("****************************************************");
      System.out.println("memberId= " + memberId);
      DiaryResponseDTO result = diaryService.createDiary(memberId, input);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.info(e.getMessage());
      return ResponseEntity.status(ErrorCode.DIARY_NOT_FOUND.getStatus()).body(null);
    }
  }

  @MutationMapping
  public ResponseEntity<DiaryResponseDTO> updateDiary(
      @Argument Long DiaryId, @Argument CreateDiaryInput input) {
    DiaryResponseDTO result = diaryService.updateDiary(DiaryId, input);
    return ResponseEntity.ok(result);
  }

  @QueryMapping
  public ResponseEntity<DiaryConnectionDTO> allDiaries(
      @Argument Integer first, @Argument String after) {
    DiaryConnectionDTO result = diaryService.getPublicDiaryList(first, after);
    return ResponseEntity.ok(result);
  }

  @QueryMapping
  public ResponseEntity<DiaryResponseDTO> diaryById(@Argument Long id) {
    DiaryResponseDTO result = diaryService.getDiaryById(id);
    return ResponseEntity.ok(result);
  }

  @QueryMapping
  public ResponseEntity<List<DiaryResponseDTO>> myDiaries() {
    Long memberId = CurrentUserContext.getCurrentMemberId();
    List<DiaryResponseDTO> result = diaryService.getMyDiaries(memberId);
    return ResponseEntity.ok(result);
  }

  @MutationMapping
  public Boolean deleteDiary(@Argument Long DiaryId) {
    try {
      diaryService.deleteDiary(DiaryId);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @MutationMapping
  public Integer addDiaryLike(@Argument Long id) {
    try {
      Long memberId = CurrentUserContext.getCurrentMemberId();
      return diaryService.createDiaryLike(id, memberId);
    } catch (CustomException e) {
      return 0;
    }
  }

  @MutationMapping
  public Integer cancelDiaryLike(@Argument Long diaryId, @Argument Long memberId) {
    // memberId = CurrentUserContext.getCurrentMemberId();
    return diaryService.cancelDiaryLike(diaryId, memberId);
  }

  @QueryMapping
  public Boolean isDiaryWrittenByMe(@Argument Long id) {
    Long memberId = CurrentUserContext.getCurrentMemberId();
    return diaryService.isMine(id, memberId);
  }

  @QueryMapping
  public Boolean isDiaryLikedByMe(@Argument Long id) {
    Long memberId = CurrentUserContext.getCurrentMemberId();
    return diaryService.checkIfPressLikeByMe(id, memberId);
  }

  @QueryMapping
  public ResponseEntity<List<DiaryResponseDTO>> randomDiaries(@Argument Integer first) {
    try {
      Long memberId = CurrentUserContext.getCurrentMemberId();
      List<DiaryResponseDTO> result = diaryService.getRandomDiaries(first, memberId);
      return ResponseEntity.ok(result);
    } catch (CustomException e) {

      return ResponseEntity.status(ErrorCode.MEMBER_NOT_FOUND.getStatus())
          .body(null); // message를 담기 위해 나중에 수정
    }
  }

  // reportDiary(input: CreateDiaryReportInput!): Boolean!
  @MutationMapping
  public ResponseEntity<Boolean> reportDiary(@Argument DiaryReportDTO input) {
    try {
      Boolean check = diaryService.makeDiaryReport(input);
      return ResponseEntity.ok(check);
    } catch (CustomException e) {
      return ResponseEntity.status(ErrorCode.DIARYREPORT_NOT_FOUND.getStatus()).body(false);
    }
  }

  //  @QueryMapping
  //  public ResponseEntity<DiaryConnectionDTO> diariesList(
  //          @Argument Integer first, @Argument String after) {
  //    DiaryConnectionDTO result = diaryService.getPublicDiaries(first, after);
  //    return ResponseEntity.ok(result);
  //  }

}
