package com.toba.toba.service;

import java.util.List;

import com.toba.toba.dto.TeamRequestDto;
import com.toba.toba.dto.TeamResponseDto;

public interface TeamService {

	List<TeamResponseDto> findAll();

	TeamResponseDto findById(Long id);

	TeamResponseDto save(TeamRequestDto dto);

	TeamResponseDto update(Long id, TeamRequestDto dto);

	void deleteById(Long id);
}
