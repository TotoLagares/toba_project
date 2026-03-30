package com.toba.toba.dto;

import java.time.LocalDateTime;

import com.toba.toba.entities.enums.ProjectStatus;

public record ProjectRequestDto(
		String name,
		String description,
		LocalDateTime startDate,
		ProjectStatus status) {
}
