package com.toba.toba.dto.ticketStageDtos;

import com.toba.toba.entities.enums.TicketState;

import java.time.LocalDate;

public record TicketStageResponseDto(
        Long ticketStageId,
        Long userId,
        Long ticketId,
        String msg,
        TicketState state,
        LocalDate createTime
) {
}
