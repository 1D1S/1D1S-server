package com.odos.odos_server.domain.member.entity;

import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberInterest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @Enumerated(EnumType.STRING)
  private ChallengeCategory category;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;

  public MemberInterest(Member member, ChallengeCategory category) {
    this.member = member;
    this.category = category;
  }
}
