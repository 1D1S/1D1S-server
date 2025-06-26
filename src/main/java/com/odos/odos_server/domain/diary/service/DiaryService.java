package com.odos.odos_server.domain.diary.service;

import com.odos.odos_server.domain.challenge.repository.ChallengeGoalRepository;
import com.odos.odos_server.domain.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryService {

  private final DiaryRepository diaryRepository;
  private final ChallengeGoalRepository challengeGoalRepository;
  // private final

}
