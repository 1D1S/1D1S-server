package com.odos.odos_server.domain.diary.service;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.entity.ChallengeGoal;
import com.odos.odos_server.domain.challenge.repository.ChallengeGoalRepository;
import com.odos.odos_server.domain.challenge.repository.ChallengeRepository;
import com.odos.odos_server.domain.common.S3Service;
import com.odos.odos_server.domain.common.dto.PageInfoDto;
import com.odos.odos_server.domain.common.dto.S3Dto;
import com.odos.odos_server.domain.diary.dto.*;
import com.odos.odos_server.domain.diary.entity.*;
import com.odos.odos_server.domain.diary.repository.*;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import com.odos.odos_server.security.util.CurrentUserContext;
import java.time.LocalDate;
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
  private final S3Service s3Service;

  /*
  Diary가 있는지 없는지 등의 권한 처리하는 코드 부족함 없는 거 있으니 추가하기
   */
  @Transactional
  public DiaryDto createDiary(Long memberId, CreateDiaryInput input) {
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    Challenge challenge =
        challengeRepository
            .findById(input.challengeId())
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

    LocalDate diaryDate = LocalDate.parse(input.achievedDate());
    Diary diary =
        Diary.builder()
            .title(input.title())
            .createdDate(LocalDate.now())
            .date(diaryDate)
            .feeling(input.feeling())
            .isPublic(input.isPublic())
            .content(input.content())
            .deleted(false)
            .member(member)
            .challenge(challenge)
            .build();

    if (input.achievedGoalIds() != null) {
      for (Long goalId : input.achievedGoalIds()) {
        ChallengeGoal cg =
            challengeGoalRepository
                .findById(goalId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_GOAL_NOT_FOUND));

        DiaryGoal diaryGoal =
            DiaryGoal.builder()
                .goalCompleted(true)
                .challengeGoal(cg)
                .memberChallenge(cg.getMemberChallenge())
                .build();
        diary.addDiaryGoal(diaryGoal);
      }
    }

    diaryRepository.save(diary);
    return DiaryDto.from(diary, diaryLikeRepository.findDiaryLikesByDiaryId(diary.getId()));
  }

  @Transactional // 수정완료
  public DiaryDto updateDiary(Long diaryId, CreateDiaryInput input) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

    Challenge challenge =
        challengeRepository
            .findById(input.challengeId())
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

    LocalDate diaryDate = LocalDate.parse(input.achievedDate());
    diary.update(
        input.title(), input.content(), input.feeling(), input.isPublic(), diaryDate, challenge);

    // 이미지 삭제
    if (diary.getDiaryImages() != null && !diary.getDiaryImages().isEmpty()) {
      diaryImageRepository.deleteAll(diary.getDiaryImages());
    }

    // 목표 삭제
    if (diary.getDiaryGoals() != null && !diary.getDiaryGoals().isEmpty()) {
      diaryGoalRepository.deleteAll(diary.getDiaryGoals());
      diary.getDiaryGoals().clear();
    }

    // 새로운 목표 추가
    if (input.achievedGoalIds() != null) {
      for (Long goalId : input.achievedGoalIds()) {
        ChallengeGoal cg =
            challengeGoalRepository
                .findById(goalId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_GOAL_NOT_FOUND));

        DiaryGoal dg =
            DiaryGoal.builder()
                .goalCompleted(true)
                .challengeGoal(cg)
                .memberChallenge(cg.getMemberChallenge())
                .diary(diary)
                .build();
        diary.addDiaryGoal(dg);
      }
    }

    diaryRepository.save(diary);
    return DiaryDto.from(diary, diaryLikeRepository.findDiaryLikesByDiaryId(diary.getId()));
  }

  public List<String> addDiaryImg(Long diaryId, List<String> fileNameList) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
    diary.getDiaryImages().clear();

    List<String> presignedUrls = new ArrayList<>();
    for (String fileName : fileNameList) {
      S3Dto s3Dto = s3Service.generatePresignedUrl(fileName);
      presignedUrls.add(s3Dto.presignedUrl());

      diaryImageRepository.save(DiaryImage.builder().diary(diary).url(s3Dto.key()).build());
    }

    return presignedUrls;
  }

  @Transactional
  public List<DiaryDto> getAllDiary() {
    List<Diary> diaries = diaryRepository.findAllPublicDiaries();
    List<DiaryDto> result = new ArrayList<>();
    for (Diary d : diaries) {
      result.add(DiaryDto.from(d, diaryLikeRepository.findDiaryLikesByDiaryId(d.getId())));
    }
    return result;
  }

  @Transactional(readOnly = true)
  public DiaryConnectionDto getPublicDiaryList(Integer first, String after) {
    int size = (first != null) ? first : 10;
    int page = decodePageCursor(after); // 커서 디코딩

    long totalCount = diaryRepository.countByIsPublicTrue();
    int startOffset = page * size;

    // 전체 개수보다 큰 offset 요청 시 빈 페이지 반환
    if (startOffset >= totalCount) {
      return new DiaryConnectionDto(
          Collections.emptyList(), new PageInfoDto(null, false), (int) totalCount);
    }

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
    Page<Diary> pageResult = diaryRepository.findByIsPublicTrue(pageable);
    List<Diary> diaries = pageResult.getContent();

    List<DiaryEdgeDto> edges = new ArrayList<>();
    for (int i = 0; i < diaries.size(); i++) {
      Diary d = diaries.get(i);
      if (d == null || d.getId() == null) continue; // null 방어

      String cursor = encodeItemCursor(startOffset + i);
      edges.add(
          new DiaryEdgeDto(
              DiaryDto.from(d, diaryLikeRepository.findDiaryLikesByDiaryId(d.getId())), cursor));
    }

    String endCursor = pageResult.hasNext() ? encodePageCursor(page + 1) : null;
    PageInfoDto pageInfo = new PageInfoDto(endCursor, pageResult.hasNext());

    return new DiaryConnectionDto(edges, pageInfo, (int) totalCount);
  }

  private int decodePageCursor(String cursor) {
    if (cursor == null || cursor.isEmpty()) return 0;
    try {
      return Integer.parseInt(new String(Base64.getDecoder().decode(cursor)));
    } catch (Exception e) {
      System.err.println("잘못된 after 커서: " + cursor);
      return 0;
    }
  }

  /* 페이지네이션 관련 메서드 */
  private String encodePageCursor(int page) {
    return Base64.getEncoder().encodeToString(String.valueOf(page).getBytes());
  }

  private String encodeItemCursor(long offset) {
    return Base64.getEncoder().encodeToString(String.valueOf(offset).getBytes());
  }

  @Transactional
  public DiaryDto getDiaryById(Long diaryId) {
    List<DiaryLike> diaryLikes = diaryLikeRepository.findDiaryLikesByDiaryId(diaryId);
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new CustomException(ErrorCode.DIARYLIKE_NOT_FOUND));
    return DiaryDto.from(diary, diaryLikes);
  }

  @Transactional(readOnly = true)
  public List<DiaryDto> getMyDiaries(Long memberId) {
    List<Diary> myDiaries = diaryRepository.findAllByMyId(memberId);

    List<DiaryDto> result = new ArrayList<>();

    for (Diary d : myDiaries) {
      result.add(DiaryDto.from(d, diaryLikeRepository.findDiaryLikesByDiaryId(d.getId())));
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
  public Integer createDiaryLike(Long diaryId, Long memberId) { // 성공
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
      throw new CustomException(ErrorCode.DIARYLIKE_ALREADY_FOUND);
    }
  }

  @Transactional
  public Integer cancelDiaryLike(Long diaryId, Long memberId) { // 성공
    Optional<DiaryLike> like =
        diaryLikeRepository.findDiaryLikeByMemberIdAndDiaryId(memberId, diaryId);
    // DiaryLike가 없어서 예외처리 던지면 GrapahQL 스키마 상 무조건 INT! 리턴이라 에러터짐
    if (like.isEmpty()) return 0;
    else {
      diaryLikeRepository.delete(like.get());
      List<DiaryLike> likeList = diaryLikeRepository.findDiaryLikesByDiaryId(diaryId);
      return likeList.size();
    }
  }

  @Transactional
  public Boolean isMine(Long diaryId, Long memberId) { //
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
  public List<DiaryDto> getRandomDiaries(Integer first, Long memberId) {
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
        .map(d -> DiaryDto.from(d, diaryLikeRepository.findDiaryLikesByDiaryId(d.getId())))
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
