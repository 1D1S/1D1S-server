package com.odos.odos_server.domain.challenge.entity;

import com.odos.odos_server.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "ChallengeLike")
@Builder
public class ChallengeLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challengeId")
  private Challenge challenge;
}
