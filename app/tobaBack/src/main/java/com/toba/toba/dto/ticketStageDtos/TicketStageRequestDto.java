package com.toba.toba.dto.ticketStageDtos;

import com.toba.toba.entities.enums.TicketState;
import jakarta.validation.constraints.NotBlank;

public record TicketStageRequestDto(
        String msg,
        @NotBlank(message = "El estado del ticketStage no puede estar vacio")
        TicketState state,
        @NotBlank(message = "El userId que creo el ticket no puede estar vacio")
        Long userId,
        @NotBlank(message = "El ticket al que pertenece este stage no puede estar vacio")
        Long ticketId
) {
}
