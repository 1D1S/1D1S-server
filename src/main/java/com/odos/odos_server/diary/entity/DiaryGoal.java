package com.odos.odos_server.diary.entity;

// 챌린지 목표-일지

import com.odos.odos_server.challenge.entity.ChallengeGoal;
import com.odos.odos_server.challenge.entity.MemberChallenge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "DiaryGoal")
public class DiaryGoal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private Boolean goalCompleted;

  @ManyToOne
  @JoinColumn(name = "diaryId")
  private Diary diary; // 일자 id

  @ManyToOne
  @JoinColumn(name = "challengeGoalId")
  private ChallengeGoal challengeGoal; // 챌린지 목표 아이디

  @ManyToOne
  @JoinColumn(name = "memberChallengeId")
  private MemberChallenge memberChallenge;
}
