package com.toba.toba.service;

import java.util.List;

import com.toba.toba.dto.ticketDos.TicketRequestDto;
import com.toba.toba.dto.ticketDos.TicketResponseDto;
import com.toba.toba.dto.ticketStageDtos.TicketStageRequestDto;
import com.toba.toba.dto.ticketStageDtos.TicketStageResponseDto;

public interface TicketService {

	List<TicketResponseDto> findAll();

	TicketResponseDto findById(Long id);

	TicketResponseDto save(TicketRequestDto dto);

	TicketResponseDto update(Long id, TicketRequestDto dto);

	void deleteById(Long id);

	TicketStageResponseDto addStage(TicketStageRequestDto dto);
}
