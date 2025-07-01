package com.odos.odos_server.domain.diary.controller;

import com.odos.odos_server.domain.diary.dto.CreateDiaryInput;
import com.odos.odos_server.domain.diary.dto.CreateDiaryReportInput;
import com.odos.odos_server.domain.diary.dto.DiaryConnectionDTO;
import com.odos.odos_server.domain.diary.dto.DiaryResponseDTO;
import com.odos.odos_server.domain.diary.service.DiaryService;
import com.odos.odos_server.error.exception.CustomException;
import com.odos.odos_server.security.util.CurrentUserContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DiaryController {

  private final DiaryService diaryService;

  @MutationMapping
  public DiaryResponseDTO createDiary(@Argument CreateDiaryInput input) {
    try {
      Long memberId = CurrentUserContext.getCurrentMemberId();
      System.out.println("****************************************************");
      System.out.println("memberId= " + memberId);
      return diaryService.createDiary(memberId, input);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return null;
  }

  @MutationMapping
  public DiaryResponseDTO updateDiary(@Argument Long DiaryId, @Argument CreateDiaryInput input) {
    return diaryService.updateDiary(DiaryId, input);
  }

  @QueryMapping
  public DiaryConnectionDTO allDiaries(@Argument Integer first, @Argument String after) {
    return diaryService.getPublicDiaryList(first, after);
  }

  @QueryMapping
  public DiaryResponseDTO diaryById(@Argument Long id) {
    return diaryService.getDiaryById(id);
  }

  @QueryMapping
  public List<DiaryResponseDTO> myDiaries() {
    Long memberId = CurrentUserContext.getCurrentMemberId();
    return diaryService.getMyDiaries(memberId);
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
  public Integer addDiaryLike(@Argument Long id, @Argument Long memberId) {
    try {
      return diaryService.createDiaryLike(id, memberId);
    } catch (CustomException e) {
      log.info(e.getMessage());
      return null;
    }
  }

  @MutationMapping
  public Integer cancelDiaryLike(@Argument Long diaryId, @Argument Long memberId) {
    try {
      diaryService.cancelDiaryLike(diaryId, memberId);
    } catch (CustomException e) {
      log.info(e.getMessage());
    }
    return null;
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
  public List<DiaryResponseDTO> randomDiaries(@Argument Integer first) {
    try {
      Long memberId = CurrentUserContext.getCurrentMemberId();
      return diaryService.getRandomDiaries(first, memberId);

    } catch (CustomException e) {
      log.info(e.getMessage());
      return null;
    }
  }

  // reportDiary(input: CreateDiaryReportInput!): Boolean!
  @MutationMapping
  public Boolean reportDiary(@Argument CreateDiaryReportInput input) {
    try {
      return diaryService.makeDiaryReport(input);
    } catch (CustomException e) {
      log.info(e.getMessage());
      return null;
    }
  }

  //  @QueryMapping
  //  public ResponseEntity<DiaryConnectionDTO> diariesList(
  //          @Argument Integer first, @Argument String after) {
  //    DiaryConnectionDTO result = diaryService.getPublicDiaries(first, after);
  //    return ResponseEntity.ok(result);
  //  }

}
