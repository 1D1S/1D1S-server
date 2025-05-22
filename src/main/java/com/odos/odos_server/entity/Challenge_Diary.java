package com.odos.odos_server.entity;

// 챌린지 목표-일지

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Challenge_Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long challege_diary_id;

  @Column private Boolean isChallengeSuccess;

  @ManyToOne
  @JoinColumn(name = "diary_id")
  private Diary diary; // 일자 id

  @ManyToOne
  @JoinColumn(name = "challenge_goal_id")
  private Challenge_Goal challengeGoal; // 챌린지 목표 아이디

  @ManyToOne
  @JoinColumn(name = "member_challenge_id")
  private Member_Challenge memberChallenge;
}
