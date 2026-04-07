package com.toba.toba.mapper;

import java.util.List;

import com.toba.toba.dto.teamDtos.TeamRequestDto;
import com.toba.toba.dto.teamDtos.TeamResponseDto;
import com.toba.toba.entities.Team;
import com.toba.toba.entities.User;

public final class TeamMapper {

	private TeamMapper() {
	}

	public static TeamResponseDto toResponseDto(Team team) {
		List<Long> memberIds = team.getTeamMembers() == null ? List.of()
				: team.getTeamMembers().stream().map(User::getId).toList();
		return new TeamResponseDto(team.getId(), team.getTeamType(), memberIds);
	}

	public static Team toEntity(TeamRequestDto dto) {
		return Team.builder()
				.teamType(dto.teamType())
				.build();
	}
}
