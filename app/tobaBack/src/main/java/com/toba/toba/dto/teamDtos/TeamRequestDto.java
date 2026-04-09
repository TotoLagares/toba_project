package com.toba.toba.dto.teamDtos;

import com.toba.toba.entities.enums.TeamType;
import jakarta.validation.constraints.NotBlank;

public record TeamRequestDto(
        @NotBlank(message = "El nombre del equipo no puede estar vacio")
        String name,
        @NotBlank(message = "El tipo de equipo no puede estar vacio")
        TeamType teamType
        ) {}
