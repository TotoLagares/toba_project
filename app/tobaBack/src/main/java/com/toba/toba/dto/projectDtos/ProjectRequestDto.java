package com.toba.toba.dto.projectDtos;

import java.time.LocalDateTime;
import java.util.List;

import com.toba.toba.entities.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;
import org.aspectj.bridge.Message;

public record ProjectRequestDto(
        @NotBlank(message = "El nombre no puede estar vacio")
		String name,
		String description,
        List<Long> teamIds,
		LocalDateTime startDate,
        @NotBlank(message = "El status no puede estar vacio")
		ProjectStatus status) {
}