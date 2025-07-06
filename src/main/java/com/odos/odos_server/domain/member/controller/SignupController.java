package com.odos.odos_server.domain.member.controller;

import com.odos.odos_server.domain.common.dto.S3Dto;
import com.odos.odos_server.domain.member.dto.SignupInfoRequest;
import com.odos.odos_server.domain.member.service.SignupService;
import com.odos.odos_server.security.jwt.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {

  private final SignupService signupService;

  @PutMapping("/info")
  public ResponseEntity<S3Dto> completeSignupInfo(
      @RequestBody SignupInfoRequest request, @AuthenticationPrincipal MemberPrincipal principal) {

    S3Dto s3Dto = signupService.completeSignupInfo(principal.getId(), request);

    return ResponseEntity.ok(s3Dto);
  }
}
