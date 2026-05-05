package com.toba.toba.dto.projectDtos;

import java.time.LocalDateTime;
import java.util.List;

import com.toba.toba.entities.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectRequestDto(
        @NotBlank(message = "El nombre no puede estar vacio")
		String name,
		String description,
        List<Long> teamIds,
		LocalDateTime startDate,
        @NotNull(message = "El status no puede estar vacio")
		ProjectStatus status) {
}