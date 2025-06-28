package com.odos.odos_server.domain.challenge.dto;

import lombok.Getter;

@Getter
public record ChallengeEdgeDto(
        ChallengeDto challengeDto, String cursor
) {
}
