package com.toba.toba.dto.teamDtos;

import java.util.List;

import com.toba.toba.entities.enums.TeamType;

public record TeamResponseDto(
		Long id,
		TeamType teamType,
		List<Long> memberIds) {
}
