package com.odos.odos_server.domain.member.entity;

import com.odos.odos_server.domain.Enum.Interest;
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
  private Interest category;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;
}
