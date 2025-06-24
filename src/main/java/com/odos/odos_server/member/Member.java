package com.odos.odos_server.member;

import com.odos.odos_server.Enum.*;
import com.odos.odos_server.challenge.Challenge;
import com.odos.odos_server.challenge.ChallengeLike;
import com.odos.odos_server.diary.Diary;
import com.odos.odos_server.diary.DiaryLike;
import com.odos.odos_server.diary.DiaryReport;
import com.odos.odos_server.friend.Friend;
import com.odos.odos_server.interest.Interest;
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
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @Column private String memberEmail;

  @Column
  @Enumerated(EnumType.STRING)
  private SignupRoute memberSignupRoute;

  @Column private String nickname;

  @Column private String memberProfileImageUrl;

  @Column
  @Enumerated(EnumType.STRING)
  private Job memberJob;

  @Column private LocalDateTime memberBirth;

  @Column
  @Enumerated(EnumType.STRING)
  private Gender memberGender;

  @Column private Boolean memberPublic;

  @Column
  @Enumerated(EnumType.STRING)
  private Role memberRole;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Challenge> challenges;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<MemberChallenge> memberChallenges;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<ChallengeLike> challengeLikes;

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private List<Friend> senders;

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private List<Friend> receivers;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Interest> interests;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<DiaryLike> diaryLikes;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<DiaryReport> diaryReports;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Diary> diaries;
}
