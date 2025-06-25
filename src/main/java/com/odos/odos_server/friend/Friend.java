package com.odos.odos_server.friend;

import com.odos.odos_server.Enum.FriendStatus;
import com.odos.odos_server.member.entity.Member;
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
  private Long id;

  @Column
  @Enumerated(EnumType.STRING)
  private FriendStatus friendStatus;

  @ManyToOne
  @JoinColumn(name = "memberSend")
  private Member sender;

  @ManyToOne
  @JoinColumn(name = "memberReceive")
  private Member receiver;
}
