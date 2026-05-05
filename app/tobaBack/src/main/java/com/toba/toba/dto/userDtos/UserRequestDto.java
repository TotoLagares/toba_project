package com.toba.toba.dto.userDtos;

import java.time.LocalDateTime;

import com.toba.toba.entities.enums.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDto(
        @NotBlank(message = "El nombre del usuario no puede estar vacio")
		String name,
        @NotBlank(message = "El apellido del usuario no puede estar vacio")
		String surname,
        @NotBlank(message = "El mail del usuario no puede estar vacio")
		String mail,
        @NotNull(message = "El rol del usuario no puede estar vacio")
		RoleEnum role,
        @NotNull(message = "El team al que pertence el usuario no puede estar vacio")
		Long teamId,
        @NotNull(message = "La direccion del usuario no puede estar vacia")
		AdressNested address,
        @NotNull(message = "Las credenciales del usuario no pueden estar vacias")
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
