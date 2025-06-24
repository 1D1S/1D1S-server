package com.odos.odos_server.challenge;

import com.odos.odos_server.Enum.ChallengeCategory;
import com.odos.odos_server.Enum.ChallengeType;
import com.odos.odos_server.diary.Diary;
import com.odos.odos_server.member.Member;
import com.odos.odos_server.member.MemberChallenge;
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

  @Column private ChallengeCategory challengeCategory;

  @Column private LocalDateTime challengeStartDate;

  @Column private LocalDateTime challengeEndDate;

  @Column private Long challengeParticipantsCnt;

  @Column
  @Enumerated(EnumType.STRING)
  private ChallengeType challengeType;

  @Column private String challengeDescription;

  @ManyToOne
  @JoinColumn(name = "challengeHost")
  private Member hostMember;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<MemberChallenge> memberChallenges;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<ChallengeLike> challengeLikes;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<ChallengeImage> challengeImages;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<Diary> challengeDiaries;
}
