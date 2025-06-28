package com.odos.odos_server.domain.challenge.dto;

import com.odos.odos_server.domain.common.dto.PageInfoDto;
import java.util.List;

public record ChallengeConnectionDto(List<ChallengeEdgeDto> edges, PageInfoDto pageInfoDto) {}
