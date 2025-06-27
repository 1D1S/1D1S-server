// src/main/java/com/odos/odos_server/domain/diary/service/DiaryService.java
package com.odos.odos_server.domain.diary.service;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.entity.ChallengeGoal;
import com.odos.odos_server.domain.challenge.repository.ChallengeGoalRepository;
import com.odos.odos_server.domain.challenge.repository.ChallengeRepository;
import com.odos.odos_server.domain.diary.dto.CreateDiaryInput;
import com.odos.odos_server.domain.diary.dto.DateInput;
import com.odos.odos_server.domain.diary.entity.*;
import com.odos.odos_server.domain.diary.repository.*;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {
  private final DiaryRepository diaryRepository;
  private final DiaryImageRepository diaryImageRepository;
  private final DiaryGoalRepository diaryGoalRepository;
  private final MemberRepository memberRepository;
  private final ChallengeRepository challengeRepository;
  private final ChallengeGoalRepository challengeGoalRepository;

  @Transactional
  public Diary createDiary(Long memberId, CreateDiaryInput input) {
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    Challenge challenge =
        challengeRepository
            .findById(input.getChallengeId())
            .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

    // 추후에 DateDTO인가 DateInputDTO로 바꿔야함
    DateInput dateInput = input.getAchievedDate();
    LocalDateTime diaryDate =
        LocalDateTime.of(dateInput.getYear(), dateInput.getMonth(), dateInput.getDay(), 0, 0);

    Diary diary =
        new Diary(
            null,
            input.getTitle(),
            null,
            diaryDate,
            input.getFeeling(),
            input.getIsPublic(),
            input.getContent(),
            false,
            null,
            null,
            member,
            challenge,
            null);

    diary = diaryRepository.save(diary);

    if (input.getImages() != null) {
      for (String url : input.getImages()) {
        diaryImageRepository.save(new DiaryImage(null, url, diary));
      }
    }

    if (input.getGoalIds() != null) {
      for (Long goalId : input.getGoalIds()) {
        ChallengeGoal cg =
            challengeGoalRepository
                .findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
        diaryGoalRepository.save(new DiaryGoal(null, true, diary, cg, null));
      }
    }
    return diary;
  }

  @Transactional
  public Diary updateDiary(Long diaryId, CreateDiaryInput input) {
    Diary diary =
        diaryRepository
            .findById(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("Diary not found"));

    Challenge challenge =
        challengeRepository
            .findById(input.getChallengeId())
            .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

    DateInput dateInput = input.getAchievedDate();
    LocalDateTime diaryDate =
        LocalDateTime.of(dateInput.getYear(), dateInput.getMonth(), dateInput.getDay(), 0, 0);

    diary.update(input.getTitle(), input.getContent(), input.getFeeling(), diaryDate, challenge);

    diary.getDiaryImages().clear(); // 이걸 없애면 기존 사진에 더하는 로직으로 변경될 수 있음 그래서 넣기?
    if (input.getImages() != null) {
      for (String url : input.getImages()) {
        diaryImageRepository.save(new DiaryImage(null, url, diary));
      }
    }

    diary.getDiaryGoals().clear();
    if (input.getGoalIds() != null) {
      for (Long goalId : input.getGoalIds()) {
        ChallengeGoal cg =
            challengeGoalRepository
                .findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
        diaryGoalRepository.save(new DiaryGoal(null, true, diary, cg, null));
      }
    }

    return diaryRepository.save(diary);
  }

  @Transactional
  public List<Diary> getAllDiary() {
    return diaryRepository.findAll();
  }

  @Transactional
  public Diary getDiaryById(Long diaryId) {
    return diaryRepository
        .findById(diaryId)
        .orElseThrow(() -> new IllegalArgumentException("Diary not found"));
  }

  @Transactional(readOnly = true)
  public List<Diary> getMyDiaries(Long memberId) {
    List<Diary> myDiaries = diaryRepository.findAllByMyId(memberId);
    System.out.println("총 개수: " + myDiaries.size());
    for (Diary d : myDiaries) {
      System.out.println(d.getTitle());
    }
  }
}
