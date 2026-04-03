package com.toba.toba.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toba.toba.dto.userDtos.UserRequestDto;
import com.toba.toba.dto.userDtos.UserResponseDto;
import com.toba.toba.entities.Adress;
import com.toba.toba.entities.Credentials;
import com.toba.toba.entities.Team;
import com.toba.toba.entities.User;
import com.toba.toba.exception.ResourceNotFoundException;
import com.toba.toba.mapper.UserMapper;
import com.toba.toba.repository.TeamRepository;
import com.toba.toba.repository.UserRepository;
import com.toba.toba.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final TeamRepository teamRepository;

	@Override
	public List<UserResponseDto> findAll() {
		return userRepository.findAll().stream().map(UserMapper::toResponseDto).toList();
	}

	@Override
	public UserResponseDto findById(Long id) {
		return userRepository.findById(id)
				.map(UserMapper::toResponseDto)
				.orElseThrow(() -> new ResourceNotFoundException("User", id));
	}

	@Override
	@Transactional
	public UserResponseDto save(UserRequestDto dto) {
		Team team = resolveTeam(dto);
		User saved = userRepository.save(UserMapper.toEntity(dto, team));
		return UserMapper.toResponseDto(saved);
	}

	@Override
	@Transactional
	public UserResponseDto update(Long id, UserRequestDto dto) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", id));
		Team team = resolveTeam(dto);

		user.setName(dto.name());
		user.setSurname(dto.surname());
		user.setMail(dto.mail());
		user.setRole(dto.role());
		user.setTeam(team);

		if (dto.adress() == null) {
			user.setAdress(null);
		} else if (user.getAdress() == null) {
			user.setAdress(Adress.builder()
					.street(dto.adress().street())
					.zipCode(dto.adress().zipCode())
					.houseNumber(dto.adress().houseNumber())
					.build());
		} else {
			user.getAdress().setStreet(dto.adress().street());
			user.getAdress().setZipCode(dto.adress().zipCode());
			user.getAdress().setHouseNumber(dto.adress().houseNumber());
		}

		if (dto.credentials() == null) {
			user.setCredentials(null);
		} else if (user.getCredentials() == null) {
			user.setCredentials(Credentials.builder()
					.userField(dto.credentials().userField())
					.password(dto.credentials().password())
					.lastLogin(dto.credentials().lastLogin())
					.build());
		} else {
			user.getCredentials().setUserField(dto.credentials().userField());
			user.getCredentials().setPassword(dto.credentials().password());
			user.getCredentials().setLastLogin(dto.credentials().lastLogin());
		}

		return UserMapper.toResponseDto(userRepository.save(user));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User", id);
		}
		userRepository.deleteById(id);
	}

	private Team resolveTeam(UserRequestDto dto) {
		if (dto.teamId() == null) {
			return null;
		}
		return teamRepository.findById(dto.teamId())
				.orElseThrow(() -> new ResourceNotFoundException("Team", dto.teamId()));
	}
}
