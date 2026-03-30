package com.toba.toba.mapper;

import com.toba.toba.dto.TicketRequestDto;
import com.toba.toba.dto.TicketResponseDto;
import com.toba.toba.entities.Project;
import com.toba.toba.entities.Ticket;

public final class TicketMapper {

	private TicketMapper() {
	}

	public static TicketResponseDto toResponseDto(Ticket ticket) {
		Long projectId = ticket.getProject() != null ? ticket.getProject().getId() : null;
		return new TicketResponseDto(
				ticket.getId(),
				ticket.getTopic(),
				ticket.getPriority(),
				ticket.getState(),
				projectId);
	}

	public static Ticket toEntity(TicketRequestDto dto, Project project) {
		return Ticket.builder()
				.topic(dto.topic())
				.priority(dto.priority())
				.state(dto.state())
				.project(project)
				.build();
	}
}
