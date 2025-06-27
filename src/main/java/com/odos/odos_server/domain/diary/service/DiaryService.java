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
    return null;
  }

  @Transactional
  public List<Diary> getAllDiary() {
    return null;
  }

  @Transactional
  public Diary getOneDiary() {
    return null;
  }
}
