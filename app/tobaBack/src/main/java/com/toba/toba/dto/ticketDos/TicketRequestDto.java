package com.toba.toba.dto.ticketDos;

import com.toba.toba.entities.enums.PriorityEnum;
import com.toba.toba.entities.enums.TicketState;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TicketRequestDto(
        @NotBlank(message = "El topic no puede estar vacio")
		String topic,
        @NotBlank(message = "La prioridad no puede estar vacio")
		PriorityEnum priority,
		TicketState currentState,
        @NotBlank(message = "El proyecto al que pertenece el ticket no puede estar vacio")
		Long projectId) {
}
