package com.toba.toba.mapper;

import java.util.List;

import com.toba.toba.dto.projectDtos.ProjectRequestDto;
import com.toba.toba.dto.projectDtos.ProjectResponseDto;
import com.toba.toba.entities.Project;
import com.toba.toba.entities.Ticket;

public final class ProjectMapper {

	private ProjectMapper() {
	}

	public static ProjectResponseDto toResponseDto(Project project) {
		List<Long> ticketIds = project.getAllTickets() == null ? List.of()
				: project.getAllTickets().stream().map(Ticket::getId).toList();
		return new ProjectResponseDto(
				project.getId(),
				project.getName(),
				project.getDescription(),
				project.getStartDate(),
				project.getStatus(),
				ticketIds);
	}

	public static Project toEntity(ProjectRequestDto dto) {
		return Project.builder()
				.name(dto.name())
				.description(dto.description())
				.startDate(dto.startDate())
				.status(dto.status())
				.build();
	}
}
