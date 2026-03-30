package com.toba.toba.service;

import java.util.List;

import com.toba.toba.dto.TicketRequestDto;
import com.toba.toba.dto.TicketResponseDto;

public interface TicketService {

	List<TicketResponseDto> findAll();

	TicketResponseDto findById(Long id);

	TicketResponseDto save(TicketRequestDto dto);

	TicketResponseDto update(Long id, TicketRequestDto dto);

	void deleteById(Long id);
}
