package com.toba.toba.dto;

import com.toba.toba.entities.enums.PriorityEnum;
import com.toba.toba.entities.enums.TicketState;

public record TicketResponseDto(
		Long id,
		String topic,
		PriorityEnum priority,
		TicketState state,
		Long projectId) {
}
