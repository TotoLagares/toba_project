package com.toba.toba.service;

import java.util.List;

import com.toba.toba.dto.userDtos.UserRequestDto;
import com.toba.toba.dto.userDtos.UserResponseDto;

public interface UserService {

	List<UserResponseDto> findAll();

	UserResponseDto findById(Long id);

	UserResponseDto save(UserRequestDto dto);

	UserResponseDto update(Long id, UserRequestDto dto);

	void deleteById(Long id);
}
