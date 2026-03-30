package com.toba.toba.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toba.toba.dto.TicketRequestDto;
import com.toba.toba.dto.TicketResponseDto;
import com.toba.toba.entities.Project;
import com.toba.toba.entities.Ticket;
import com.toba.toba.exception.ResourceNotFoundException;
import com.toba.toba.mapper.TicketMapper;
import com.toba.toba.repository.ProjectRepository;
import com.toba.toba.repository.TicketRepository;
import com.toba.toba.service.TicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

	private final TicketRepository ticketRepository;
	private final ProjectRepository projectRepository;

	@Override
	public List<TicketResponseDto> findAll() {
		return ticketRepository.findAll().stream().map(TicketMapper::toResponseDto).toList();
	}

	@Override
	public TicketResponseDto findById(Long id) {
		return ticketRepository.findById(id)
				.map(TicketMapper::toResponseDto)
				.orElseThrow(() -> new ResourceNotFoundException("Ticket", id));
	}

	@Override
	@Transactional
	public TicketResponseDto save(TicketRequestDto dto) {
		if (dto.projectId() == null) {
			throw new IllegalArgumentException("projectId es obligatorio");
		}
		Project project = projectRepository.findById(dto.projectId())
				.orElseThrow(() -> new ResourceNotFoundException("Project", dto.projectId()));
		Ticket saved = ticketRepository.save(TicketMapper.toEntity(dto, project));
		return TicketMapper.toResponseDto(saved);
	}

	@Override
	@Transactional
	public TicketResponseDto update(Long id, TicketRequestDto dto) {
		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ticket", id));
		if (dto.projectId() == null) {
			throw new IllegalArgumentException("projectId es obligatorio");
		}
		ticket.setTopic(dto.topic());
		ticket.setPriority(dto.priority());
		ticket.setState(dto.state());
		Project project = projectRepository.findById(dto.projectId())
				.orElseThrow(() -> new ResourceNotFoundException("Project", dto.projectId()));
		ticket.setProject(project);
		return TicketMapper.toResponseDto(ticketRepository.save(ticket));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		if (!ticketRepository.existsById(id)) {
			throw new ResourceNotFoundException("Ticket", id);
		}
		ticketRepository.deleteById(id);
	}
}
