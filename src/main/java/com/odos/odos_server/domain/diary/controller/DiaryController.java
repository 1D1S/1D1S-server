package com.odos.odos_server.domain.diary.controller;

import com.odos.odos_server.domain.diary.dto.CreateDiaryInput;
import com.odos.odos_server.domain.diary.dto.CreateDiaryReportInput;
import com.odos.odos_server.domain.diary.dto.DiaryConnectionDto;
import com.odos.odos_server.domain.diary.dto.DiaryDto;
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
  public DiaryDto createDiary(@Argument CreateDiaryInput input) {
    try {
      Long memberId = CurrentUserContext.getCurrentMemberId();
      return diaryService.createDiary(memberId, input);
    } catch (Exception e) {
      log.error("createDiary 내부 에러 발생", e);
      throw e; // 다시 던져야 GraphQL에 전달됨
    }
  }

  @MutationMapping
  public DiaryDto updateDiary(@Argument Long DiaryId, @Argument CreateDiaryInput input) {
    return diaryService.updateDiary(DiaryId, input);
  }

  @QueryMapping // memberId 없이 다 보일 수 있게 또 수정?
  public List<DiaryDto> allDiaries() {
    try {
      return diaryService.getAllDiary();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
      // throw new GraphQlClientException("알 수 없는 오류가 발생했습니다.");
    }
  }

  @QueryMapping
  public DiaryDto diaryById(@Argument Long id) {
    try {
      return diaryService.getDiaryById(id);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }

  @QueryMapping
  public List<DiaryDto> myDiaries() {
    try {
      Long memberId = CurrentUserContext.getCurrentMemberId();
      return diaryService.getMyDiaries(memberId);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }

  @MutationMapping
  public Boolean deleteDiary(@Argument Long DiaryId) {
    try {
      return diaryService.deleteDiary(DiaryId);
    } catch (CustomException e) {
      return false;
    }
  }

  @MutationMapping
  public Integer addDiaryLike(@Argument Long diaryId, @Argument Long memberId) {
    return diaryService.createDiaryLike(diaryId, memberId);
  }

  @MutationMapping // 수정 필요, 삭제 되고 조회하려고 함
  public Integer cancelDiaryLike(@Argument Long diaryId, @Argument Long memberId) {
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
  public List<DiaryDto> randomDiaries(@Argument Integer first) {
    try {
      Long memberId = CurrentUserContext.getCurrentMemberId();
      return diaryService.getRandomDiaries(first, memberId);

    } catch (CustomException e) {
      log.info(e.getMessage());
      return null;
    }
  }

  @MutationMapping
  public Boolean reportDiary(@Argument CreateDiaryReportInput input) {
    try {
      return diaryService.makeDiaryReport(input);
    } catch (CustomException e) {
      log.info(e.getMessage());
      return null;
    }
  }

  @QueryMapping
  public DiaryConnectionDto diariesList(@Argument Integer first, @Argument String after) {
    return diaryService.getPublicDiaryList(first, after);
  }

  //  @QueryMapping
  //  public ResponseEntity<DiaryConnectionDTO> diariesList(
  //          @Argument Integer first, @Argument String after) {
  //    DiaryConnectionDTO result = diaryService.getPublicDiaries(first, after);
  //    return ResponseEntity.ok(result);
  //  }

  @MutationMapping
  public List<String> addDiaryImg(@Argument Long diaryId, @Argument List<String> fileNameList) {
    return diaryService.addDiaryImg(diaryId, fileNameList);
  }
}
