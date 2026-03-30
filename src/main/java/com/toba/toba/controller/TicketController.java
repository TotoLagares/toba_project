package com.toba.toba.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.toba.toba.dto.TicketRequestDto;
import com.toba.toba.dto.TicketResponseDto;
import com.toba.toba.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;

	@GetMapping
	public List<TicketResponseDto> getAll() {
		return ticketService.findAll();
	}

	@GetMapping("/{id}")
	public TicketResponseDto getById(@PathVariable Long id) {
		return ticketService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TicketResponseDto create(@RequestBody TicketRequestDto dto) {
		return ticketService.save(dto);
	}

	@PutMapping("/{id}")
	public TicketResponseDto update(@PathVariable Long id, @RequestBody TicketRequestDto dto) {
		return ticketService.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		ticketService.deleteById(id);
	}
}
