package com.odos.odos_server.domain.member.dto;

import java.util.List;

public record MemberInfoDto(String job, List<String> category, String birthday, String gender) {}
