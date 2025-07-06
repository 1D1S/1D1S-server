package com.odos.odos_server.domain.member.service;

import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.common.S3Service;
import com.odos.odos_server.domain.common.dto.S3Dto;
import com.odos.odos_server.domain.member.dto.SignupInfoRequest;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

  private final MemberRepository memberRepository;
  private final S3Service s3Service;

  @Transactional
  public S3Dto completeSignupInfo(Long memberId, SignupInfoRequest request) {
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    List<ChallengeCategory> list = request.getCategory();
    if (list == null || list.isEmpty()) {
      throw new CustomException(ErrorCode.CATEGORY_EMPTY);
    }
    if (list.size() > 3) {
      throw new CustomException(ErrorCode.CATEGORY_TOO_MANY);
    }

    String regex = "^[가-힣a-zA-Z]{1,8}$";
    if (!request.getNickname().matches(regex)) {
      throw new CustomException(ErrorCode.INVALID_NICKNAME_FORMAT);
    }

    String profileFileName = request.getProfileFileName();
    S3Dto s3Dto = s3Service.generatePresignedUrl(profileFileName);

    member.completeProfile(
        request.getNickname(),
        s3Dto.key(),
        request.getJob(),
        request.getBirth(),
        request.getGender(),
        request.getIsPublic());

    member.updateCategories(list);

    memberRepository.save(member);

    return s3Dto;
  }
}
