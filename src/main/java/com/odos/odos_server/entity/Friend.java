package com.odos.odos_server.entity;

import com.odos.odos_server.Enum.Friend_Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Friend {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long friend_id;

  @Column
  @Enumerated(EnumType.STRING)
  private Friend_Status friend_status;

  @ManyToOne
  @JoinColumn(name = "member_send")
  private Member sender;

  @ManyToOne
  @JoinColumn(name = "member_receive")
  private Member receiver;
}
