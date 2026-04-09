package com.toba.toba.dto.projectDtos;

import java.time.LocalDateTime;
import java.util.List;

import com.toba.toba.entities.enums.ProjectStatus;

public record ProjectResponseDto(
		Long id,
		String name,
		String description,
		LocalDateTime startDate,
		ProjectStatus status,
        List<Long> teamIds,
		List<Long> ticketIds) {
}
