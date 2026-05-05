package com.toba.toba.dto.ticketStageDtos;

import com.toba.toba.entities.enums.TicketState;
import jakarta.validation.constraints.NotNull;

public record TicketStageRequestDto(
        String msg,
        @NotNull(message = "El estado del ticketStage no puede estar vacio")
        TicketState state,
        @NotNull(message = "El userId que creo el ticket no puede estar vacio")
        Long userId,
        @NotNull(message = "El ticket al que pertenece este stage no puede estar vacio")
        Long ticketId
) {
}
