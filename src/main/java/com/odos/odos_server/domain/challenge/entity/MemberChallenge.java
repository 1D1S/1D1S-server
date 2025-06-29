package com.odos.odos_server.domain.challenge.entity;

import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import com.odos.odos_server.domain.member.entity.Member;
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
@Table(name = "MemberChallenge")
@Builder
public class MemberChallenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @Enumerated(EnumType.STRING)
  private MemberChallengeRole memberChallengeRole;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challengeId")
  private Challenge challenge;

  @OneToMany(mappedBy = "memberChallenge", cascade = CascadeType.ALL)
  private List<ChallengeGoal> challengeGoals;

  public void setMemberChallengeRole(MemberChallengeRole memberChallengeRole) {
    this.memberChallengeRole = memberChallengeRole;
  }
}
