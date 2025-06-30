// src/main/java/com/odos/odos_server/domain/diary/service/DiaryService.java
package com.odos.odos_server.domain.diary.service;

import com.odos.odos_server.domain.challenge.repository.ChallengeGoalRepository;
import com.odos.odos_server.domain.challenge.repository.ChallengeRepository;
import com.odos.odos_server.domain.common.dto.LikesDto;
import com.odos.odos_server.domain.common.dto.PageInfoDto;
import com.odos.odos_server.domain.diary.dto.*;
import com.odos.odos_server.domain.diary.entity.*;
import com.odos.odos_server.domain.diary.repository.*;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import com.odos.odos_server.security.util.CurrentUserContext;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {
  private final DiaryRepository diaryRepository;
  private final DiaryLikeRepository diaryLikeRepository;
  private final DiaryImageRepository diaryImageRepository;
  private final DiaryReportRepository diaryReportRepository;
  private final DiaryGoalRepository diaryGoalRepository;
  private final MemberRepository memberRepository;
  private final ChallengeRepository challengeRepository;
  private final ChallengeGoalRepository challengeGoalRepository;

  @Transactional
  public DiaryResponseDTO createDiary(Long memberId, CreateDiaryInput input) {
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    //    Challenge challenge =
    //        challengeRepository
    //            .findById(input.getChallengeId())
    //            .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

    // 추후에 DateDTO인가 DateInputDTO로 바꿔야함
    DateInput dateInput = input.achievedDate();
    LocalDateTime diaryDate =
        LocalDateTime.of(dateInput.year(), dateInput.month(), dateInput.day(), 0, 0);

    Diary diary =
        new Diary(
            null,
            input.title(),
            null,
            diaryDate,
            input.feeling(),
            input.isPublic(),
            input.content(),
            false,
            null,
            null,
            null,
            member,
            null,
            null);

    diary = diaryRepository.save(diary);

    if (input.images() != null) {
      for (String url : input.images()) {
        diaryImageRepository.save(new DiaryImage(null, url, diary));
      }
    }

    //    if (input.getGoalIds() != null) {
    //      for (Long goalId : input.getGoalIds()) {
    //        ChallengeGoal cg =
    //            challengeGoalRepository
    //                .findById(goalId)
    //                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
    //        diaryGoalRepository.save(new DiaryGoal(null, true, diary, cg, null));
    //      }
    //    }
    return DiaryResponseDTO.from(diary, diary.getDiaryLikes());
  }

  @Transactional
  public DiaryResponseDTO updateDiary(Long diaryId, CreateDiaryInput input) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("Diary not found"));

    //    Challenge challenge =
    //        challengeRepository
    //            .findById(input.getChallengeId())
    //            .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

    DateInput dateInput = input.achievedDate();
    LocalDateTime diaryDate =
        LocalDateTime.of(dateInput.year(), dateInput.month(), dateInput.day(), 0, 0);

    diary.update(
        input.title(),
        input.content(),
        input.feeling(),
        input.isPublic(),
        diaryDate); // , challenge);

    diary.getDiaryImages().clear(); // 이걸 없애면 기존 사진에 더하는 로직으로 변경될 수 있음
    if (input.images() != null) {
      for (String url : input.images()) {
        diaryImageRepository.save(new DiaryImage(null, url, diary));
      }
    }

    //    diary.getDiaryGoals().clear();
    //    if (input.getGoalIds() != null) {
    //      for (Long goalId : input.getGoalIds()) {
    //        ChallengeGoal cg =
    //            challengeGoalRepository
    //                .findById(goalId)
    //                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
    //        diaryGoalRepository.save(new DiaryGoal(null, true, diary, cg, null));
    //      }
    //    }

    diaryRepository.save(diary);
    return DiaryResponseDTO.from(diary, diary.getDiaryLikes());
  }

  @Transactional
  public List<DiaryResponseDTO> getAllDiary() { // 공개된 다이어리 최신순 전체정렬 : 10개씩 페이지네이션은 아직 NO..
    List<Diary> diaries = diaryRepository.findAllPublicDiaries();
    List<DiaryResponseDTO> result = new ArrayList<>();
    for (Diary d : diaries) {
      result.add(DiaryResponseDTO.from(d, d.getDiaryLikes()));
    }
    return result;
  }

  @Transactional(readOnly = true)
  public DiaryConnectionDTO getPublicDiaryList(Integer first, String after) {
    int size = (first != null) ? first : 10;
    int page = decodePageCursor(after); // 커서 디코딩

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
    Page<Diary> pageResult = diaryRepository.findByIsPublicTrue(pageable);

    List<DiaryEdgeDTO> edges = new ArrayList<>();
    int startOffset = page * size;
    List<Diary> diaries = pageResult.getContent();

    for (int i = 0; i < diaries.size(); i++) {
      Diary d = diaries.get(i);
      String cursor = encodeItemCursor(startOffset + i);
      edges.add(new DiaryEdgeDTO(DiaryResponseDTO.from(d, d.getDiaryLikes()), cursor));
    }

    String endCursor = pageResult.hasNext() ? encodePageCursor(page + 1) : null;
    PageInfoDto pageInfo = new PageInfoDto(endCursor, pageResult.hasNext());
    long totalCount = diaryRepository.countByIsPublicTrue();

    return new DiaryConnectionDTO(edges, pageInfo, (int) totalCount);
  }

  /* 페이지네이션 관련 메서드 */
  private String encodePageCursor(int page) {
    return Base64.getEncoder().encodeToString(String.valueOf(page).getBytes());
  }

  private int decodePageCursor(String cursor) {
    if (cursor == null || cursor.isEmpty()) {
      return 0;
    }
    try {
      return Integer.parseInt(new String(Base64.getDecoder().decode(cursor)));
    } catch (Exception e) {
      return 0;
    }
  }

  private String encodeItemCursor(long offset) {
    return Base64.getEncoder().encodeToString(String.valueOf(offset).getBytes());
  }

  @Transactional
  public DiaryResponseDTO getDiaryById(Long diaryId) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("Diary not found"));
    return DiaryResponseDTO.from(diary, diary.getDiaryLikes());
  }

  @Transactional(readOnly = true) // query의 myDiaries
  public List<DiaryResponseDTO> getMyDiaries(Long memberId) {
    List<Diary> myDiaries = diaryRepository.findAllByMyId(memberId);
    List<DiaryResponseDTO> result = new ArrayList<>();
    System.out.println("총 개수: " + myDiaries.size());
    for (Diary d : myDiaries) {
      System.out.println(d.getTitle());
    }
    for (Diary d : myDiaries) {
      result.add(DiaryResponseDTO.from(d, d.getDiaryLikes()));
    }
    return result;
  }

  @Transactional
  public Boolean deleteDiary(Long diaryId) {
    try {
      Diary diary =
          diaryRepository
              .findById(diaryId)
              .orElseThrow(
                  () -> new IllegalArgumentException(ErrorCode.DIARY_NOT_FOUND.getMessage()));
      diaryRepository.delete(diary);
    } catch (Exception e) {
      throw new IllegalArgumentException(ErrorCode.DIARY_NOT_FOUND.getMessage());
    }
    return false;
  }

  @Transactional
  public Integer createDiaryLike(Long diaryId, Long memberId) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    DiaryLike like = new DiaryLike(null, member, diary);
    diaryLikeRepository.save(like);

    List<DiaryLike> likes = diaryLikeRepository.findAll();
    return LikesDto.fromDiary(likes).count();
  }

  @Transactional
  public Integer cancelDiaryLike(Long diaryId, Long memberId) {
    DiaryLike diaryLike = diaryLikeRepository.findDiaryLikeByMemberIdAndDiaryId(memberId, diaryId);
    diaryLikeRepository.delete(diaryLike);
    List<DiaryLike> diaryLikes = diaryLikeRepository.findAll();
    return LikesDto.fromDiary(diaryLikes).count();
  }

  @Transactional
  public Boolean isMine(Long diaryId, Long memberId) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
    if ((diary.getMember().getId()).equals(memberId)) {
      return true;
    }
    return false;
  }

  @Transactional
  public Boolean checkIfPressLikeByMe(Long diaryId, Long memberId) {
    DiaryLike like =
        diaryLikeRepository
            .findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARYLIKE_NOT_FOUND));
    if ((like.getMember().getId()).equals(memberId)) {
      return true;
    }
    return false;
  }

  @Transactional
  public List<DiaryResponseDTO> getRandomDiaries(Integer first, Long memberId) {
    /*
    비가입 회원에게는 리스트만 보이고(내용은 프론트가 골라서 넣어주는것?),
    상세접근은 못함, 하려고하면 로그인요구
     */
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    List<Diary> diaries = diaryRepository.findAll();

    List<DiaryResponseDTO> result = new ArrayList<>();
    if (diaries.size() <= first) {
      for (Diary d : diaries) {
        DiaryResponseDTO dr = DiaryResponseDTO.from(d, d.getDiaryLikes());
        result.add(dr);
      }
    } else {
      Set<Integer> selectDiaries = new HashSet<>();

      while (result.size() < first) {
        int randomIndex = (int) (Math.random() * diaries.size());
        if (!selectDiaries.contains(randomIndex)) {
          Diary d = diaries.get(randomIndex);
          result.add(DiaryResponseDTO.from(d, d.getDiaryLikes()));
          selectDiaries.add(randomIndex);
        }
      }
    }
    return result;
  }

  @Transactional
  public Boolean makeDiaryReport(DiaryReportDTO input) {
    Long memberId = CurrentUserContext.getCurrentMemberId();
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    Diary diary =
        diaryRepository
            .findById(input.diaryId())
            .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
    DiaryReport report = new DiaryReport(null, input.reportType(), input.content(), member, diary);
    diaryReportRepository.save(report);
    return true;
  }
}
