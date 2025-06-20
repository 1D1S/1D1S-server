package com.odos.odos_server.member;

import com.odos.odos_server.Enum.*;
import com.odos.odos_server.challenge.Challenge;
import com.odos.odos_server.challenge.ChallengeLike;
import com.odos.odos_server.diary.Diary;
import com.odos.odos_server.diary.Diary_Like;
import com.odos.odos_server.diary.Diary_Report;
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
  private Long member_id;

  @Column private String member_email;

  @Column
  @Enumerated(EnumType.STRING)
  private Signup_Route member_signup_route;

  @Column private String nickname;

  @Column private String member_profile_image_url;

  @Column
  @Enumerated(EnumType.STRING)
  private Job member_job;

  @Column private LocalDateTime member_birth;

  @Column
  @Enumerated(EnumType.STRING)
  private Gender member_gender;

  @Column private Boolean member_public;

  @Column
  @Enumerated(EnumType.STRING)
  private Role member_role;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Challenge> challenges;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Member_Challenge> memberChallenges;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<ChallengeLike> challengeLikes;

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private List<Friend> senders;

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private List<Friend> receivers;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Interest> interests;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Diary_Like> diaryLikes;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Diary_Report> diaryReports;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Diary> diaries;
}
