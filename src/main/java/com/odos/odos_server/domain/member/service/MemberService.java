package com.odos.odos_server.domain.member.service;

import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.common.S3Service;
import com.odos.odos_server.domain.common.dto.S3Dto;
import com.odos.odos_server.domain.member.dto.UpdateMemberProfileInput;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final S3Service s3Service;

  @Transactional(readOnly = true)
  public Member getMemberById(Long memberId) {
    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public List<Member> getAllMembers() {
    return memberRepository.findAll();
  }

  @Transactional
  public Member updateMemberProfile(Long memberId, UpdateMemberProfileInput in) {
    Member m =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (in.getNickname() != null) {
      validateNicknameSpelling(in.getNickname());
      validateNicknameTime(m);
      m.updateNickname(in.getNickname());
    }
    // if (in.getProfileImageUrl() != null) m.updateProfileImageUrl(in.getProfileImageUrl());
    if (in.getJob() != null) m.updateJob(in.getJob());
    if (in.getIsPublic() != null) m.updateIsPublic(in.getIsPublic());

    List<ChallengeCategory> list = in.getCategory();

    if (list == null || list.isEmpty()) {
      throw new CustomException(ErrorCode.CATEGORY_EMPTY);
    }
    if (list.size() > 3) {
      throw new CustomException(ErrorCode.CATEGORY_TOO_MANY);
    }
    m.updateCategories(list);

    return m;
  }

  private void validateNicknameTime(Member member) {
    LocalDateTime lastModified = member.getNicknameLastModifiedAt();
    if (lastModified != null && lastModified.plusMonths(1).isAfter(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.NICKNAME_CHANGE_TOO_SOON);
    }
  }

  public void validateNicknameSpelling(String nickname) {
    String regex = "^[가-힣a-zA-Z]{1,8}$";
    if (!nickname.matches(regex)) {
      throw new CustomException(ErrorCode.INVALID_NICKNAME_FORMAT);
    }
  }

  @Transactional
  public boolean deleteById(Long memberId) {
    if (!memberRepository.existsById(memberId)) return false;
    memberRepository.deleteById(memberId);
    return true;
  }

  @Transactional
  public String updateMemberProfileImg(Long id, String fileName) {
    Member member =
        memberRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    S3Dto s3Dto = s3Service.generatePresignedUrl(fileName);
    member.updateProfileImageKey(s3Dto.key());
    return s3Dto.presignedUrl();
  }
}
