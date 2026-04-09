package com.toba.toba.service.impl;

import java.util.List;

import com.toba.toba.entities.Team;
import com.toba.toba.entities.TeamProject;
import com.toba.toba.repository.TeamProjectRepository;
import com.toba.toba.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toba.toba.dto.projectDtos.ProjectRequestDto;
import com.toba.toba.dto.projectDtos.ProjectResponseDto;
import com.toba.toba.entities.Project;
import com.toba.toba.exception.ResourceNotFoundException;
import com.toba.toba.mapper.ProjectMapper;
import com.toba.toba.repository.ProjectRepository;
import com.toba.toba.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final TeamProjectRepository teamProjectRepository;

	@Override
	public List<ProjectResponseDto> findAll() {
		return projectRepository.findAll().stream().map(ProjectMapper::toResponseDto).toList();
	}

	@Override
	public ProjectResponseDto findById(Long id) {
		return projectRepository.findById(id)
				.map(ProjectMapper::toResponseDto)
				.orElseThrow(() -> new ResourceNotFoundException("Project", id));
	}

	@Override
    @Transactional
    public ProjectResponseDto save(ProjectRequestDto dto) {
        Project project = ProjectMapper.toEntity(dto);
        projectRepository.save(project);
        // Chequeo que los TeamsIds que se ingresaron existan y si existen creo un TeamProject con esos datos.
        if (dto.teamIds() != null) {
            for (Long teamId : dto.teamIds()) {
                Team team = teamRepository.findById(teamId)
                        .orElseThrow(() -> new ResourceNotFoundException("Team", teamId));

                // Guardo el proyecto y el team en TeamProyect
                TeamProject tp = TeamProject.builder()
                        .project(project)
                        .team(team)
                        .build();
                // Guardo el TeamProject en su repostorio
                teamProjectRepository.save(tp);
            }
        }

        return ProjectMapper.toResponseDto(projectRepository.findById(project.getId()).get());
    }

	@Override
	@Transactional
	public ProjectResponseDto update(Long id, ProjectRequestDto dto) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project", id));
		project.setName(dto.name());
		project.setDescription(dto.description());
		project.setStartDate(dto.startDate());
		project.setStatus(dto.status());
		return ProjectMapper.toResponseDto(projectRepository.save(project));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		if (!projectRepository.existsById(id)) {
			throw new ResourceNotFoundException("Project", id);
		}
		projectRepository.deleteById(id);
	}
}
