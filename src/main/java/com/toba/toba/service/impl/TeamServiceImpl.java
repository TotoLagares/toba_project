package com.toba.toba.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toba.toba.dto.TeamRequestDto;
import com.toba.toba.dto.TeamResponseDto;
import com.toba.toba.entities.Team;
import com.toba.toba.exception.ResourceNotFoundException;
import com.toba.toba.mapper.TeamMapper;
import com.toba.toba.repository.TeamRepository;
import com.toba.toba.service.TeamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

	private final TeamRepository teamRepository;


	@Override
	public List<TeamResponseDto> findAll() {
		return teamRepository.findAll().stream().map(TeamMapper::toResponseDto).toList();
	}

	@Override
	public TeamResponseDto findById(Long id) {
		return teamRepository.findById(id)
				.map(TeamMapper::toResponseDto)
				.orElseThrow(() -> new ResourceNotFoundException("Team", id));
	}

	@Override
	@Transactional
	public TeamResponseDto save(TeamRequestDto dto) {
		Team saved = teamRepository.save(TeamMapper.toEntity(dto));
		return TeamMapper.toResponseDto(saved);
	}

	@Override
	@Transactional
	public TeamResponseDto update(Long id, TeamRequestDto dto) {
		Team team = teamRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Team", id));
		team.setTeamType(dto.teamType());
		return TeamMapper.toResponseDto(teamRepository.save(team));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		if (!teamRepository.existsById(id)) {
			throw new ResourceNotFoundException("Team", id);
		}
		teamRepository.deleteById(id);
	}
}
