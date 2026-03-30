package com.toba.toba.mapper;

import com.toba.toba.dto.UserRequestDto;
import com.toba.toba.dto.UserResponseDto;
import com.toba.toba.dto.UserResponseDto.AdressResponse;
import com.toba.toba.dto.UserResponseDto.CredentialsResponse;
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
		if (user.getAdress() != null) {
			Adress a = user.getAdress();
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
		Adress adress = null;
		if (dto.adress() != null) {
			adress = Adress.builder()
					.street(dto.adress().street())
					.zipCode(dto.adress().zipCode())
					.houseNumber(dto.adress().houseNumber())
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
				.adress(adress)
				.credentials(credentials)
				.build();
	}
}
