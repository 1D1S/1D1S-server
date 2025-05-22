package com.odos.odos_server.entity;

import com.odos.odos_server.Enum.Challenge_Status;
import com.odos.odos_server.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member_Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long member_challenge_id;

  @Column
  @Enumerated(EnumType.STRING)
  private Challenge_Status member_challenge_status;

  @Column
  @Enumerated(EnumType.STRING)
  private Role member_challenge_role;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challenge_id")
  private Challenge challenge;
}
