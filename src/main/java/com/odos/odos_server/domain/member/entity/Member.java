package com.odos.odos_server.domain.member.entity;

import com.odos.odos_server.domain.Enum.Gender;
import com.odos.odos_server.domain.Enum.Job;
import com.odos.odos_server.domain.Enum.Role;
import com.odos.odos_server.domain.Enum.SignupRoute;
import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.entity.ChallengeLike;
import com.odos.odos_server.domain.challenge.entity.MemberChallenge;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import com.odos.odos_server.domain.diary.entity.DiaryReport;
import com.odos.odos_server.domain.friend.Friend;
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
  private Long id;

  @Column private String email;

  @Column
  @Enumerated(EnumType.STRING)
  private SignupRoute signupRoute;

  @Column private String nickname;

  @Column private String profileImageUrl;

  @Column
  @Enumerated(EnumType.STRING)
  private Job job;

  @Column private LocalDateTime birth;

  @Column
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column private Boolean isPublic;

  @Column
  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "hostMember", cascade = CascadeType.ALL)
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
  private List<MemberInterest> memberInterests;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<DiaryLike> diaryLikes;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<DiaryReport> diaryReports;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Diary> diaries;
}
