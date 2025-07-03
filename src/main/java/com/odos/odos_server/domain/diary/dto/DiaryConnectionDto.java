package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.dto.PageInfoDto;
import java.util.List;

/** Connection DTO wrapping paginated diaries. */
public record DiaryConnectionDto(List<DiaryEdgeDto> edges, PageInfoDto pageInfo, int totalCount) {}
