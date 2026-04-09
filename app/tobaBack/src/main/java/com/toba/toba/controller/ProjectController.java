package com.toba.toba.controller;

import java.util.List;

import jakarta.validation.Valid;
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

import com.toba.toba.dto.projectDtos.ProjectRequestDto;
import com.toba.toba.dto.projectDtos.ProjectResponseDto;
import com.toba.toba.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@GetMapping
	public List<ProjectResponseDto> getAll() {
		return projectService.findAll();
	}

	@GetMapping("/{id}")
	public ProjectResponseDto getById(@PathVariable Long id) {
		return projectService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProjectResponseDto create(@RequestBody @Valid ProjectRequestDto dto) {
		return projectService.save(dto);
	}

	@PutMapping("/{id}")
	public ProjectResponseDto update(@PathVariable Long id, @Valid @RequestBody ProjectRequestDto dto) {
		return projectService.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		projectService.deleteById(id);
	}
}
