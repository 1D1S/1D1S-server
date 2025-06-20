package com.odos.odos_server.challenge;

import com.odos.odos_server.member.Member_Challenge;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChallengeGoal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long challengeGoalId;

  @Column private String challengeGoalContent;

  @OneToMany(mappedBy = "challengeGoal", cascade = CascadeType.ALL)
  private List<ChallengeDiary> challengeDiaries;

  @ManyToOne
  @JoinColumn(name = "member_challenge_id")
  private Member_Challenge memberChallenge;
}
