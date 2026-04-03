package com.toba.toba.service.impl;

import java.util.List;
import java.util.Optional;

import com.toba.toba.dto.ticketStageDtos.TicketStageRequestDto;
import com.toba.toba.dto.ticketStageDtos.TicketStageResponseDto;
import com.toba.toba.entities.TicketStage;
import com.toba.toba.entities.User;
import com.toba.toba.mapper.TicketStageMapper;
import com.toba.toba.repository.TicketStageRepository;
import com.toba.toba.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toba.toba.dto.ticketDos.TicketRequestDto;
import com.toba.toba.dto.ticketDos.TicketResponseDto;
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
	private final UserRepository userRepository;
	private final TicketStageRepository ticketStageRepository;

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

		//validación de que el proyecto exista
		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ticket", id));

		//validación de que el dto tenga el id del project
		if (dto.projectId() == null) {
			throw new IllegalArgumentException("projectId es obligatorio");
		}

		ticket.setTopic(dto.topic());
		ticket.setPriority(dto.priority());
		ticket.setCurrentState(dto.currentState());
		//validación de que el project con el id del dto exista
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

	@Override
	@Transactional
	public TicketStageResponseDto addStage(TicketStageRequestDto dto) {
		//validación de que el user exista
		User user = userRepository.findById(dto.userId())
				.orElseThrow(() -> new ResourceNotFoundException("User", dto.userId()));
		//validacion de que el ticket exista
		Ticket ticket = ticketRepository.findById(dto.ticketId())
				.orElseThrow(() -> new ResourceNotFoundException("Ticket", dto.ticketId()));

		//guardo el ticketStage
		TicketStage ticketStage = TicketStageMapper.toEntity(dto,user, ticket);

		//guardo el ticketStage dentro de ticketHistory
		ticket.getTicketStory().add(ticketStage);
		ticketRepository.save(ticket);

		return TicketStageMapper.toResponseDto(ticketStage);
	}
}
