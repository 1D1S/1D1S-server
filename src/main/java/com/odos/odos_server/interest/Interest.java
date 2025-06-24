package com.odos.odos_server.interest;

import com.odos.odos_server.Enum.InterestType;
import com.odos.odos_server.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Interest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @Enumerated(EnumType.STRING)
  private InterestType category;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;
}
