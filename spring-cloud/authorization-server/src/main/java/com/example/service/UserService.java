package com.example.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.UserEntity;
import com.example.exception.UserAlreadyFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.UserDTO;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;

	public UserDTO create(UserDTO userDTO) {
		log.info("create: Entering userDTO.getUsername(): {}", userDTO.getUsername());
		Optional<UserEntity> optionalPrevUser = userRepository.findByUsername(userDTO.getUsername());
		if (optionalPrevUser.isPresent()) {
			throw new UserAlreadyFoundException();
		}
		userDTO.setId(UUID.randomUUID().toString());
		userDTO.setRoles("ROLE_USER");
		userDTO.setPassword(encoder.encode(userDTO.getPassword()));

		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(userDTO, user);
		userRepository.save(user);
		log.info("Saved user with id: {}", user.getId());

		userDTO.setPassword(null);
		return userDTO;
	}

	public UserDTO get(String id) {
		Optional<UserEntity> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException();
		}
		UserDTO userDTO = new UserDTO();
		UserEntity user = optionalUser.get();
		log.info("Found user with id: {}", user.getId());
		BeanUtils.copyProperties(user, userDTO);
		return userDTO;
	}
}
