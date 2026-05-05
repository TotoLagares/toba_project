package com.toba.toba.dto.ticketDos;

import com.toba.toba.entities.enums.PriorityEnum;
import com.toba.toba.entities.enums.TicketState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TicketRequestDto(
        @NotBlank(message = "El topic no puede estar vacio")
		String topic,
        @NotNull(message = "La prioridad no puede estar vacio")
		PriorityEnum priority,
		TicketState currentState,
        @NotNull(message = "El proyecto al que pertenece el ticket no puede estar vacio")
		Long projectId) {
}
