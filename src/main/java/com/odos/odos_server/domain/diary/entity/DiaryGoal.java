package com.odos.odos_server.domain.diary.entity;

import com.odos.odos_server.domain.challenge.entity.ChallengeGoal;
import com.odos.odos_server.domain.challenge.entity.MemberChallenge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "DiaryGoal")
public class DiaryGoal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private Boolean goalCompleted = false; // 기본값 false로 설정

  @ManyToOne
  @JoinColumn(name = "diaryId")
  private Diary diary;

  @ManyToOne
  @JoinColumn(name = "challengeGoalId")
  private ChallengeGoal challengeGoal;

  @ManyToOne
  @JoinColumn(name = "memberChallengeId")
  private MemberChallenge memberChallenge;

  public void setDiary(Diary diary) {
    this.diary = diary;
  }
}
