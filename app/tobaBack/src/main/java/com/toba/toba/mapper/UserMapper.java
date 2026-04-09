package com.toba.toba.mapper;

import com.toba.toba.dto.userDtos.UserRequestDto;
import com.toba.toba.dto.userDtos.UserResponseDto;
import com.toba.toba.dto.userDtos.UserResponseDto.AdressResponse;
import com.toba.toba.dto.userDtos.UserResponseDto.CredentialsResponse;
import com.toba.toba.entities.Adress;
import com.toba.toba.entities.Credentials;
import com.toba.toba.entities.Team;
import com.toba.toba.entities.User;

public final class UserMapper {

	private UserMapper() {
	}

	public static UserResponseDto toResponseDto(User user) {
		Long teamId = user.getTeam() != null ? user.getTeam().getId() : null;
		AdressResponse adressResponse = null;
		if (user.getAddress() != null) {
			Adress a = user.getAddress();
			adressResponse = new AdressResponse(a.getId(), a.getStreet(), a.getZipCode(), a.getHouseNumber());
		}
		CredentialsResponse credentialsResponse = null;
		if (user.getCredentials() != null) {
			Credentials c = user.getCredentials();
			credentialsResponse = new CredentialsResponse(c.getId(), c.getUserField(), c.getLastLogin());
		}
		return new UserResponseDto(
			    user.getId(),
				user.getName(),
				user.getSurname(),
				user.getMail(),
				user.getRole(),
				teamId,
				adressResponse,
				credentialsResponse);
	}

	public static User toEntity(UserRequestDto dto, Team team) {
		Adress address = null;
		if (dto.address() != null) {
			address = Adress.builder()
					.street(dto.address().street())
					.zipCode(dto.address().zipCode())
					.houseNumber(dto.address().houseNumber())
					.build();
		}
		Credentials credentials = null;
		if (dto.credentials() != null) {
			credentials = Credentials.builder()
					.userField(dto.credentials().userField())
					.password(dto.credentials().password())
					.lastLogin(dto.credentials().lastLogin())
					.build();
		}
		return User.builder()
				.name(dto.name())
				.surname(dto.surname())
				.mail(dto.mail())
				.role(dto.role())
				.team(team)
				.address(address)
				.credentials(credentials)
				.build();
	}
}
