package com.odos.odos_server.challenge;

import com.odos.odos_server.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChallengeLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diaryImageId;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challenge_id")
  private Challenge challenge;
}
