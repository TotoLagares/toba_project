package com.toba.toba.dto.ticketDos;

import com.toba.toba.entities.enums.PriorityEnum;
import com.toba.toba.entities.enums.TicketState;

import java.util.List;

public record TicketRequestDto(
		String topic,
		PriorityEnum priority,
		TicketState currentState,
		Long projectId) {
}
