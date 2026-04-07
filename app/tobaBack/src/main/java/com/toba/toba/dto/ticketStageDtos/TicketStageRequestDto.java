package com.toba.toba.dto.ticketStageDtos;

import com.toba.toba.entities.enums.TicketState;

public record TicketStageRequestDto(
        String msg,
        TicketState state,
        Long userId,
        Long ticketId
) {
}
