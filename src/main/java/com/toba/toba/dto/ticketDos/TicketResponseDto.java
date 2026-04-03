package com.toba.toba.dto.ticketDos;

import com.toba.toba.dto.ticketStageDtos.TicketStageResponseDto;
import com.toba.toba.entities.TicketStage;
import com.toba.toba.entities.enums.PriorityEnum;
import com.toba.toba.entities.enums.TicketState;

import java.util.List;

public record TicketResponseDto(
		Long id,
		String topic,
		PriorityEnum priority,
		TicketState currentState,
		Long projectId,
		List<TicketStageResponseDto> ticketStory
) {
}
