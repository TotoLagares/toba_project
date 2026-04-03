package com.toba.toba.mapper;

import com.toba.toba.dto.ticketDos.TicketRequestDto;
import com.toba.toba.dto.ticketDos.TicketResponseDto;
import com.toba.toba.dto.ticketStageDtos.TicketStageResponseDto;
import com.toba.toba.mapper.TicketStageMapper;
import com.toba.toba.entities.Project;
import com.toba.toba.entities.Ticket;

import java.util.List;

public final class TicketMapper {

	private TicketMapper() {
	}

	public static TicketResponseDto toResponseDto(Ticket ticket) {
		Long projectId = ticket.getProject() != null ? ticket.getProject().getId() : null;
		List<TicketStageResponseDto> stages = ticket.getTicketStory()
				.stream()
				.map(TicketStageMapper::toResponseDto)
				.toList();

		return new TicketResponseDto(
				ticket.getId(),
				ticket.getTopic(),
				ticket.getPriority(),
				ticket.getCurrentState(),
				projectId,
				stages
		);
	}

	public static Ticket toEntity(TicketRequestDto dto, Project project) {
		return Ticket.builder()
				.topic(dto.topic())
				.priority(dto.priority())
				.currentState(dto.currentState())
				.project(project)
				.build();
	}
}
