package com.odos.odos_server.challenge;

// 챌린지 목표-일지

import com.odos.odos_server.diary.Diary;
import com.odos.odos_server.member.MemberChallenge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "ChallengeDiary")
public class ChallengeDiary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long challengeDiaryId;

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
