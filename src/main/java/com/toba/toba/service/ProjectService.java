package com.toba.toba.service;

import java.util.List;

import com.toba.toba.dto.ProjectRequestDto;
import com.toba.toba.dto.ProjectResponseDto;

public interface ProjectService {

	List<ProjectResponseDto> findAll();

	ProjectResponseDto findById(Long id);

	ProjectResponseDto save(ProjectRequestDto dto);

	ProjectResponseDto update(Long id, ProjectRequestDto dto);

	void deleteById(Long id);
}
