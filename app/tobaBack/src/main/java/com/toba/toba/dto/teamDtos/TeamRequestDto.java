package com.toba.toba.dto.teamDtos;

import com.toba.toba.entities.enums.TeamType;

public record TeamRequestDto
        (String name,
         TeamType teamType
        ) {}
