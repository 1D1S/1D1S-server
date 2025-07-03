package com.odos.odos_server.domain.diary.service;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.repository.ChallengeGoalRepository;
import com.odos.odos_server.domain.challenge.repository.ChallengeRepository;
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

  /*
  Diary가 있는지 없는지 등의 권한 처리하는 코드 부족함 없는 거 있으니 추가하기
  Diary에서 빌더 패턴으로 객체 생성하기
   */
  @Transactional
  public DiaryResponseDto createDiary(Long memberId, CreateDiaryInput input) {
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    Challenge challenge =
        challengeRepository
            .findById(input.challengeId())
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

    // 추후에 DateDTO인가 DateInputDTO로 바꿔야함
    LocalDateTime diaryDate = LocalDateTime.parse(input.achievedDate());
    Diary diary =
        Diary.builder()
            .id(null)
            .title(input.title())
            .createdDate(LocalDateTime.now())
            .date(diaryDate)
            .feeling(input.feeling())
            .isPublic(input.isPublic())
            .content(input.content())
            .deleted(false) // ?
            // .diaryGoals(diaryGoalRepository.findByChallengeId())
            // .diaryImages(input.images())
            .diaryLikes(null)
            .member(member)
            .challenge(challenge)
            .diaryReports(null)
            .build();
    diary = diaryRepository.save(diary);

    if (input.images() != null) {
      for (String url : input.images()) {
        diaryImageRepository.save(new DiaryImage(null, url, diary));
      }
    }

    // 챌린지에서 목표 가져오면 이 코드 필요없을듯
    //    if (input.getGoalIds() != null) {
    //      for (Long goalId : input.getGoalIds()) {
    //        ChallengeGoal cg =
    //            challengeGoalRepository
    //                .findById(goalId)
    //                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
    //        diaryGoalRepository.save(new DiaryGoal(null, true, diary, cg, null));
    //      }
    //    }
    return DiaryResponseDto.from(diary, diary.getDiaryLikes());
  }

  @Transactional
  public DiaryResponseDto updateDiary(Long diaryId, CreateDiaryInput input) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

    //    Challenge challenge =
    //        challengeRepository
    //            .findById(input.getChallengeId())
    //            .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

    LocalDateTime diaryDate = LocalDateTime.parse(input.achievedDate());

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
    return DiaryResponseDto.from(diary, diary.getDiaryLikes());
  }

  @Transactional
  public List<DiaryResponseDto> getAllDiary() { // 공개된 다이어리 최신순 전체정렬 : 10개씩 페이지네이션은 아직 NO..
    List<Diary> diaries = diaryRepository.findAllPublicDiaries();
    List<DiaryResponseDto> result = new ArrayList<>();
    for (Diary d : diaries) {
      result.add(DiaryResponseDto.from(d, d.getDiaryLikes()));
    }
    return result;
  }

  @Transactional(readOnly = true)
  public DiaryConnectionDto getPublicDiaryList(Integer first, String after) {
    int size = (first != null) ? first : 10;
    int page = decodePageCursor(after); // 커서 디코딩

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
    Page<Diary> pageResult = diaryRepository.findByIsPublicTrue(pageable);

    List<DiaryEdgeDto> edges = new ArrayList<>();
    int startOffset = page * size;
    List<Diary> diaries = pageResult.getContent();

    for (int i = 0; i < diaries.size(); i++) {
      Diary d = diaries.get(i);
      String cursor = encodeItemCursor(startOffset + i);
      edges.add(new DiaryEdgeDto(DiaryResponseDto.from(d, d.getDiaryLikes()), cursor));
    }

    String endCursor = pageResult.hasNext() ? encodePageCursor(page + 1) : null;
    PageInfoDto pageInfo = new PageInfoDto(endCursor, pageResult.hasNext());
    long totalCount = diaryRepository.countByIsPublicTrue();

    return new DiaryConnectionDto(edges, pageInfo, (int) totalCount);
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
  public DiaryResponseDto getDiaryById(Long diaryId) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARYLIKE_NOT_FOUND));
    return DiaryResponseDto.from(diary, diary.getDiaryLikes());
  }

  @Transactional(readOnly = true) // query의 myDiaries
  public List<DiaryResponseDto> getMyDiaries(Long memberId) {
    List<Diary> myDiaries = diaryRepository.findAllByMyId(memberId);
    List<DiaryResponseDto> result = new ArrayList<>();

    for (Diary d : myDiaries) {
      result.add(DiaryResponseDto.from(d, d.getDiaryLikes()));
    }
    return result;
  }

  @Transactional
  public Boolean deleteDiary(Long diaryId) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARYLIKE_NOT_FOUND));
    diaryRepository.delete(diary);
    return true;
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

    Optional<DiaryLike> like =
        diaryLikeRepository.findDiaryLikeByMemberIdAndDiaryId(memberId, diaryId);
    if (like.isEmpty()) {
      DiaryLike newLike = new DiaryLike(null, member, diary);
      diaryLikeRepository.save(newLike);
      List<DiaryLike> diaryLikes = diaryLikeRepository.findDiaryLikesByDiaryId(diaryId);
      return diaryLikes.size();
    } else {
      return null;
    }
  }

  @Transactional
  public Integer cancelDiaryLike(Long diaryId, Long memberId) {
    DiaryLike like =
        diaryLikeRepository
            .findDiaryLikeByMemberIdAndDiaryId(memberId, diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARYLIKE_NOT_FOUND));
    diaryLikeRepository.delete(like);

    List<DiaryLike> likeList = diaryLikeRepository.findDiaryLikesByDiaryId(diaryId);
    if (likeList.isEmpty()) {
      return 0;
    } else {
      return likeList.size();
    }
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
            .findDiaryLikeByMemberIdAndDiaryId(memberId, diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARYLIKE_NOT_FOUND));
    return (like.getMember().getId()).equals(memberId);
  }

  @Transactional
  public List<DiaryResponseDto> getRandomDiaries(Integer first, Long memberId) {
    /*
    비가입 회원에게는 리스트만 보이고(내용은 프론트가 골라서 넣어주는것?),
    상세접근은 못함, 하려고하면 로그인요구
     */
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    int size = first != null ? first : 10;
    List<Diary> diaries = diaryRepository.findAllPublicDiaries();
    if (diaries.isEmpty()) {
      return Collections.emptyList();
    }

    Collections.shuffle(diaries);
    return diaries.stream()
        .limit(size)
        .map(d -> DiaryResponseDto.from(d, d.getDiaryLikes()))
        .toList();
  }

  @Transactional
  public Boolean makeDiaryReport(CreateDiaryReportInput input) {
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
