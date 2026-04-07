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

import com.toba.toba.dto.teamDtos.TeamRequestDto;
import com.toba.toba.dto.teamDtos.TeamResponseDto;
import com.toba.toba.service.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

	private final TeamService teamService;

	@GetMapping
	public List<TeamResponseDto> getAll() {
		return teamService.findAll();
	}

	@GetMapping("/{id}")
	public TeamResponseDto getById(@PathVariable Long id) {
		return teamService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TeamResponseDto create(@RequestBody TeamRequestDto dto) {
		return teamService.save(dto);
	}

	@PutMapping("/{id}")
	public TeamResponseDto update(@PathVariable Long id, @RequestBody TeamRequestDto dto) {
		return teamService.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		teamService.deleteById(id);
	}
}
