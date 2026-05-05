package com.toba.toba.dto.teamDtos;

import com.toba.toba.entities.enums.TeamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamRequestDto(
        @NotBlank(message = "El nombre del equipo no puede estar vacio")
        String name,
        @NotNull(message = "El tipo de equipo no puede estar vacio")
        TeamType teamType
        ) {}
