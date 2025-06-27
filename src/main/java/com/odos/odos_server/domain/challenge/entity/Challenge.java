package com.odos.odos_server.domain.challenge.entity;

import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.common.Enum.ChallengeType;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.member.entity.Member;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String title;

  @Column private ChallengeCategory category;

  @Column private LocalDate startDate;

  @Column private LocalDate endDate;

  @Column private int participantsCnt;

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
