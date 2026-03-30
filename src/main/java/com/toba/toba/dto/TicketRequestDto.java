package com.toba.toba.dto;

import com.toba.toba.entities.enums.PriorityEnum;
import com.toba.toba.entities.enums.TicketState;

public record TicketRequestDto(
		String topic,
		PriorityEnum priority,
		TicketState state,
		Long projectId) {
}
