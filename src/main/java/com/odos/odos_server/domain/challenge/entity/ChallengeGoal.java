package com.odos.odos_server.domain.challenge.entity;

import com.odos.odos_server.domain.diary.entity.DiaryGoal;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "ChallengeGoalRepository")
@Builder
public class ChallengeGoal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String content;

  @OneToMany(mappedBy = "challengeGoal", cascade = CascadeType.ALL)
  private List<DiaryGoal> diaryGoals;

  @ManyToOne
  @JoinColumn(name = "memberChallengeId")
  private MemberChallenge memberChallenge;
}
