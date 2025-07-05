package com.odos.odos_server.domain.challenge.service;

import com.odos.odos_server.domain.challenge.dto.ChallengeConnectionDto;
import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.challenge.dto.ChallengeEdgeDto;
import com.odos.odos_server.domain.challenge.dto.ChallengeFilterInputDto;
import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.entity.MemberChallenge;
import com.odos.odos_server.domain.challenge.repository.ChallengeLikeRepository;
import com.odos.odos_server.domain.challenge.repository.ChallengeRepository;
import com.odos.odos_server.domain.challenge.repository.MemberChallengeRepository;
import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import com.odos.odos_server.domain.common.dto.PageInfoDto;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import com.odos.odos_server.security.util.CurrentUserContext;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeQueryService {

  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
  private final ChallengeLikeRepository challengeLikeRepository;
  private final MemberChallengeRepository memberChallengeRepository;

  public List<ChallengeDto> getAllChallenges() {
    return challengeRepository.findAll().stream().map(ChallengeDto::from).toList();
  }

  public List<ChallengeDto> getMyChallenges() {
    Long currentMemberId = CurrentUserContext.getCurrentMemberId();
    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    return challengeRepository.findAll().stream()
        .filter(ch -> ch.getHostMember().getId().equals(member.getId()))
        .map(ChallengeDto::from)
        .toList();
  }

  public List<ChallengeDto> getRandomChallenges(int first) {
    List<Challenge> all = challengeRepository.findAll();
    Collections.shuffle(all);
    return all.stream().limit(first).map(ChallengeDto::from).toList();
  }

  public ChallengeDto getChallengeById(Long id) {
    Challenge challenge =
        challengeRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    return ChallengeDto.from(challenge);
  }

  public boolean getIsChallengeLikedByMe(Long challengeId) {
    Long currentMemberId = CurrentUserContext.getCurrentMemberId();
    return challengeLikeRepository.existsByChallengeIdAndMemberId(challengeId, currentMemberId);
  }

  public MemberChallengeRole getMyChallengeApplicantStatus(Long challengeId) {
    Long currentMemberId = CurrentUserContext.getCurrentMemberId();
    return memberChallengeRepository
        .findByMemberIdAndChallengeId(currentMemberId, challengeId)
        .map(MemberChallenge::getMemberChallengeRole)
        .orElse(MemberChallengeRole.NONE);
  }

  public ChallengeConnectionDto challengesList(
      ChallengeFilterInputDto filter, int first, String after) {

    Long cursorId = decodeCursorToId(after); // ✅ 커서를 id로 바꿔줌
    Pageable pageable = PageRequest.of(0, first, Sort.by("id").descending()); // ✅ 항상 첫 페이지 조회

    String keyword = (filter != null && filter.keyword() != null) ? filter.keyword() : "";

    Page<Challenge> pageResult;

    if (cursorId == null) {
      pageResult = challengeRepository.findByTitleContaining(keyword, pageable);
    } else {
      pageResult =
          challengeRepository.findByTitleContainingAndIdLessThanOrderByIdDesc(
              keyword, cursorId, pageable);
    }

    List<ChallengeEdgeDto> edges =
        pageResult.getContent().stream()
            .map(ch -> new ChallengeEdgeDto(ChallengeDto.from(ch), encodeCursor(ch.getId())))
            .toList();

    PageInfoDto pageInfo =
        new PageInfoDto(
            edges.isEmpty() ? null : edges.get(edges.size() - 1).cursor(), pageResult.hasNext());

    return new ChallengeConnectionDto(edges, pageInfo);
  }

  // ✅ 커서 인코딩
  public String encodeCursor(Long id) {
    return Base64.getEncoder().encodeToString(("cursor:" + id).getBytes());
  }

  public Long decodeCursorToId(String cursor) {
    if (cursor == null) return null;
    try {
      String decoded = new String(Base64.getDecoder().decode(cursor));
      return Long.parseLong(decoded.split(":")[1]);
    } catch (Exception e) {
      return null;
    }
  }

  /*
  public ChallengeConnectionDto getChallengesWithFilter(
      ChallengeFilterInputDto filter, int first, String after) {
    List<Challenge> all = challengeRepository.findAll();

    Long afterId = decodeCursor(after);

    Stream<Challenge> stream =
        all.stream()
            .filter(ch -> afterId == null || ch.getId() > afterId)
            .filter(ch -> filterByKeyword(ch, filter.keyword()))
            .filter(ch -> filterByStatus(ch, filter.status()))
            .filter(ch -> filterByDuration(ch, filter.durationRangeDto()));

    List<ChallengeEdgeDto> edges =
        stream
            .sorted(Comparator.comparing(Challenge::getId))
            .limit(first + 1) // hasNext 판단 위해 1개 더
            .map(ch -> new ChallengeEdgeDto(ChallengeDto.from(ch), encodeCursor(ch.getId())))
            .toList();

    boolean hasNextPage = edges.size() > first;
    if (hasNextPage) edges = edges.subList(0, first);

    String endCursor = edges.isEmpty() ? null : edges.get(edges.size() - 1).cursor();
    PageInfoDto pageInfo = new PageInfoDto(endCursor, hasNextPage);

    return new ChallengeConnectionDto(edges, pageInfo);
  }

  private String encodeCursor(Long id) {
    return Base64.getEncoder().encodeToString(("challenge:" + id).getBytes());
  }

  private Long decodeCursor(String cursor) {
    if (cursor == null) return null;
    try {
      String decoded = new String(Base64.getDecoder().decode(cursor));
      return Long.parseLong(decoded.replace("challenge:", ""));
    } catch (Exception e) {
      return null;
    }
  }



  private ChallengeStatus calculateStatus(Challenge challenge) {
    LocalDate today = LocalDate.now();
    if (today.isBefore(challenge.getStartDate())) return ChallengeStatus.RECRUITING;
    else if (today.isAfter(challenge.getEndDate())) return ChallengeStatus.COMPLETED;
    else return ChallengeStatus.IN_PROGRESS;
  }

  private boolean filterByStatus(Challenge challenge, ChallengeStatus filterStatus) {
    return filterStatus == null || calculateStatus(challenge) == filterStatus;
  }

  private boolean filterByKeyword(Challenge challenge, String keyword) {
    return keyword == null || challenge.getTitle().toLowerCase().contains(keyword.toLowerCase());
  }

  private boolean filterByDuration(Challenge challenge, DurationRangeDto range) {
    if (range == null) return true;
    long days = ChronoUnit.DAYS.between(challenge.getStartDate(), challenge.getEndDate());
    return days >= range.minDays() && days <= range.maxDays();
  }
  */
}
