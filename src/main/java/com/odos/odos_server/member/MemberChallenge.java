package com.odos.odos_server.member;

import com.odos.odos_server.Enum.ChallengeStatus;
import com.odos.odos_server.Enum.Role;
import com.odos.odos_server.challenge.Challenge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "MemberChallenge")
public class MemberChallenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberChallengeId;

  @Column
  @Enumerated(EnumType.STRING)
  private ChallengeStatus memberChallengeStatus;

  @Column
  @Enumerated(EnumType.STRING)
  private Role memberChallengeRole;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challengeId")
  private Challenge challenge;
}
