package com.toba.toba.dto;

import java.time.LocalDateTime;

import com.toba.toba.entities.enums.RoleEnum;

public record UserRequestDto(
		String name,
		String surname,
		String mail,
		RoleEnum role,
		Long teamId,
		AdressNested adress,
		CredentialsNested credentials) {

	public record AdressNested(
			String street,
			Integer zipCode,
			Integer houseNumber) {
	}

	public record CredentialsNested(
			String userField,
			String password,
			LocalDateTime lastLogin) {
	}
}
