package com.odos.odos_server.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Challenge_Goal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long challenge_goal_id;

  @Column private String challenge_goal_content;

  @OneToMany(mappedBy = "challengeGoal", cascade = CascadeType.ALL)
  private List<Challenge_Diary> challengeDiaries;

  @ManyToOne
  @JoinColumn(name = "member_challenge_id")
  private Member_Challenge memberChallenge;
}
