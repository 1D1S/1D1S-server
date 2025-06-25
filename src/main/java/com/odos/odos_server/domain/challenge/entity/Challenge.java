package com.odos.odos_server.domain.challenge.entity;

import com.odos.odos_server.domain.Enum.ChallengeCategory;
import com.odos.odos_server.domain.Enum.ChallengeType;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.member.entity.Member;
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
  private Long id;

  @Column private String title;

  @Column private ChallengeCategory category;

  @Column private LocalDateTime startDate;

  @Column private LocalDateTime endDate;

  @Column private Long participantsCnt;

  @Column
  @Enumerated(EnumType.STRING)
  private ChallengeType type;

  @Column private String description;

  @ManyToOne
  @JoinColumn(name = "challengeHost")
  private Member hostMember;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<MemberChallenge> memberChallenges;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<ChallengeLike> likes;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<ChallengeImage> images;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<Diary> diaries;
}
