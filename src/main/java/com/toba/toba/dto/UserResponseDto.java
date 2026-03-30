package com.toba.toba.dto;

import java.time.LocalDateTime;

import com.toba.toba.entities.enums.RoleEnum;

public record UserResponseDto(
		Long id,
		String name,
		String surname,
		String mail,
		RoleEnum role,
		Long teamId,
		AdressResponse adress,
		CredentialsResponse credentials) {

	public record AdressResponse(
			Long id,
			String street,
			Integer zipCode,
			Integer houseNumber) {
	}

	public record CredentialsResponse(
			Long id,
			String userField,
			LocalDateTime lastLogin) {
	}
}
