package com.toba.toba.mapper;

import com.toba.toba.dto.ticketStageDtos.TicketStageRequestDto;
import com.toba.toba.dto.ticketStageDtos.TicketStageResponseDto;
import com.toba.toba.entities.Ticket;
import com.toba.toba.entities.TicketStage;
import com.toba.toba.entities.User;

import java.time.LocalDate;

public class TicketStageMapper {
    private TicketStageMapper() {}

    public static TicketStageResponseDto toResponseDto(TicketStage ticketStage) {
        return new TicketStageResponseDto(
                ticketStage.getId(),
                ticketStage.getUser().getId(),
                ticketStage.getTicket().getId(),
                ticketStage.getMsg(),
                ticketStage.getState(),
                ticketStage.getCreateTime()
        );
    }

    public static TicketStage toEntity(TicketStageRequestDto dto, User user, Ticket ticket) {
        return TicketStage.builder()
                .msg(dto.msg())
                .state(dto.state())
                .user(user)
                .ticket(ticket)
                .build();
    }
}
