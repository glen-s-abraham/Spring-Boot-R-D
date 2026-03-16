package com.example.restfull_web_services.services.impl;

import com.example.restfull_web_services.models.User;
import com.example.restfull_web_services.dtos.UserDto;
import com.example.restfull_web_services.repositories.UserRepository;
import com.example.restfull_web_services.services.UserService;
import com.example.restfull_web_services.exceptions.UserNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private User mapToEntity(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .status(userDto.getStatus())
                .build();
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = mapToEntity(userDto);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        return mapToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Integer id, UserDto userDtoDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        if (userDtoDetails.getName() != null)
            existingUser.setName(userDtoDetails.getName());
        if (userDtoDetails.getEmail() != null)
            existingUser.setEmail(userDtoDetails.getEmail());
        if (userDtoDetails.getPassword() != null)
            existingUser.setPassword(userDtoDetails.getPassword());
        if (userDtoDetails.getRole() != null)
            existingUser.setRole(userDtoDetails.getRole());
        if (userDtoDetails.getStatus() != null)
            existingUser.setStatus(userDtoDetails.getStatus());

        User updatedUser = userRepository.save(existingUser);
        return mapToDto(updatedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }
}
