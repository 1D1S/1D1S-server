package com.odos.odos_server.domain.member.service;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.entity.ChallengeGoal;
import com.odos.odos_server.domain.challenge.entity.MemberChallenge;
import com.odos.odos_server.domain.challenge.repository.ChallengeGoalRepository;
import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.common.S3Service;
import com.odos.odos_server.domain.common.dto.S3Dto;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.member.dto.DailyStreakDto;
import com.odos.odos_server.domain.member.dto.StreakDto;
import com.odos.odos_server.domain.member.dto.UpdateMemberProfileInput;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final S3Service s3Service;

  private final ChallengeGoalRepository challengeGoalRepository;

  @Transactional(readOnly = true)
  public Member getMemberById(Long memberId) {
    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public List<Member> getAllMembers() {
    return memberRepository.findAll();
  }

  @Transactional
  public Member updateMemberProfile(Long memberId, UpdateMemberProfileInput in) {
    Member m =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (in.getNickname() != null) {
      validateNicknameSpelling(in.getNickname());
      validateNicknameTime(m);
      m.updateNickname(in.getNickname());
    }
    // if (in.getProfileImageUrl() != null) m.updateProfileImageUrl(in.getProfileImageUrl());
    if (in.getJob() != null) m.updateJob(in.getJob());
    if (in.getIsPublic() != null) m.updateIsPublic(in.getIsPublic());

    List<ChallengeCategory> list = in.getCategory();

    if (list == null || list.isEmpty()) {
      throw new CustomException(ErrorCode.CATEGORY_EMPTY);
    }
    if (list.size() > 3) {
      throw new CustomException(ErrorCode.CATEGORY_TOO_MANY);
    }
    m.updateCategories(list);

    return m;
  }

  private void validateNicknameTime(Member member) {
    LocalDateTime lastModified = member.getNicknameLastModifiedAt();
    if (lastModified != null && lastModified.plusMonths(1).isAfter(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.NICKNAME_CHANGE_TOO_SOON);
    }
  }

  public void validateNicknameSpelling(String nickname) {
    String regex = "^[가-힣a-zA-Z]{1,8}$";
    if (!nickname.matches(regex)) {
      throw new CustomException(ErrorCode.INVALID_NICKNAME_FORMAT);
    }
  }

  @Transactional
  public boolean deleteById(Long memberId) {
    if (!memberRepository.existsById(memberId)) return false;
    memberRepository.deleteById(memberId);
    return true;
  }

  @Transactional
  public String updateMemberProfileImg(Long id, String fileName) {
    Member member =
        memberRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    S3Dto s3Dto = s3Service.generatePresignedUrl(fileName);
    member.updateProfileImageKey(s3Dto.key());
    return s3Dto.presignedUrl();
  }

  public StreakDto getStreakByMemberId(Long id) {
    Member member =
        memberRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    List<Diary> diaryList = member.getDiaries();

    int[] streaks = calculateStreaks(diaryList);

    int todayGoalCount = getTodayGoalCount(id);
    int currentStreak = streaks[0];
    int totalDiaryCount = diaryList.size();
    int totalGoalCount = 0;
    int currentMonthDiaryCount = 0;
    int currentMonthGoalCount = 0;
    int maxStreak = streaks[1];
    List<DailyStreakDto> calendar = generateCalendar(diaryList);

    for (Diary d : diaryList) {
      totalGoalCount += d.getDiaryGoals().size();
      if (YearMonth.from(d.getDate()).equals(YearMonth.now())) {
        currentMonthDiaryCount += 1;
        currentMonthGoalCount += d.getDiaryGoals().size();
      }
    }
    return new StreakDto(
        todayGoalCount,
        currentStreak,
        totalDiaryCount,
        totalGoalCount,
        currentMonthDiaryCount,
        currentMonthGoalCount,
        maxStreak,
        calendar);
  }

  private List<DailyStreakDto> generateCalendar(List<Diary> diaryList) {
    YearMonth currentMonth = YearMonth.now();

    // 날짜별 개수를 저장할 Map
    Map<LocalDate, Long> diaryCountByDate =
        diaryList.stream()
            .filter(diary -> YearMonth.from(diary.getDate()).equals(currentMonth))
            .collect(Collectors.groupingBy(Diary::getDate, Collectors.counting()));

    // Map을 DailyStreakDto 리스트로 변환
    return diaryCountByDate.entrySet().stream()
        .map(entry -> new DailyStreakDto(entry.getKey().toString(), entry.getValue().intValue()))
        .sorted(Comparator.comparing(DailyStreakDto::date)) // 날짜순 정렬
        .toList();
  }

  public int getTodayGoalCount(Long memberId) {
    LocalDate today = LocalDate.now();

    List<ChallengeGoal> allGoals = challengeGoalRepository.findAll();

    return (int)
        allGoals.stream()
            .filter(
                goal -> {
                  MemberChallenge mc = goal.getMemberChallenge();
                  if (mc == null || mc.getMember() == null || mc.getChallenge() == null)
                    return false;

                  // 1. 현재 사용자 여부
                  if (!mc.getMember().getId().equals(memberId)) return false;

                  // 2. 챌린지 진행 중 여부
                  Challenge challenge = mc.getChallenge();
                  LocalDate start = challenge.getStartDate();
                  LocalDate end = challenge.getEndDate();

                  boolean started = !start.isAfter(today); // startDate ≤ today
                  boolean notEnded =
                      (end == null) || !today.isAfter(end); // today ≤ endDate or no endDate

                  return started && notEnded;
                })
            .count();
  }

  private int[] calculateStreaks(List<Diary> diaryList) {
    if (diaryList.isEmpty()) return new int[] {0, 0};
    // 일지 작성 날짜만 뽑아 Set으로 저장
    Set<LocalDate> diaryDates = diaryList.stream().map(Diary::getDate).collect(Collectors.toSet());
    System.out.println(diaryDates);

    // 오늘부터 과거로 1일씩 감소하며 currentStreak 계산
    int currentStreak = 0;
    LocalDate today = LocalDate.now();
    while (diaryDates.contains(today.minusDays(currentStreak))) {
      System.out.println(currentStreak);
      currentStreak++;
    }

    // maxStreak 계산 (전체 날짜 기준)
    List<LocalDate> sortedDates = diaryDates.stream().sorted().toList();

    int maxStreak = 0;
    int tempStreak = 1;
    for (int i = 1; i < sortedDates.size(); i++) {
      LocalDate prev = sortedDates.get(i - 1);
      LocalDate curr = sortedDates.get(i);
      if (prev.plusDays(1).equals(curr)) {
        tempStreak++;
      } else {
        maxStreak = Math.max(maxStreak, tempStreak);
        tempStreak = 1;
      }
    }
    maxStreak = Math.max(maxStreak, tempStreak); // 마지막 streak 고려

    return new int[] {currentStreak, maxStreak};
  }
}
