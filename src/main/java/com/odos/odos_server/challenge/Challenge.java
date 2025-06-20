package com.odos.odos_server.challenge;

import com.odos.odos_server.Enum.Challenge_Type;
import com.odos.odos_server.member.Member;
import com.odos.odos_server.member.Member_Challenge;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long challengeId;

  @Column private String challengeTitle;

  @Column private String challengeCategory; // enum으로 안함?

  @Column private LocalDateTime challengeStartDate;

  @Column private LocalDateTime challengeEndDate;

  @Column private Long challengeParticipantsCnt; // 수 표현 위해 cnt 붙임

  @Column
  @Enumerated(EnumType.STRING)
  private Challenge_Type challengeType;

  @Column private String challengeDescription;

  @ManyToOne
  @JoinColumn(name = "challenge_host")
  private Member member; // 챌린지가 없어진다고 해서 멤버가 없어지는건 아니니까.. 고민,, 주회자 아이디 의도하긴함

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<Member_Challenge> memberChallenges;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<ChallengeLike> challengeLikes;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<ChallengeImage> challengeImages;
}
