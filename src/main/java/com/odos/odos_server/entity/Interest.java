package com.odos.odos_server.entity;

import com.odos.odos_server.Enum.Interest_Type;
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
  private Long interest_id;

  @Column
  @Enumerated(EnumType.STRING)
  private Interest_Type interest_category;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;
}
