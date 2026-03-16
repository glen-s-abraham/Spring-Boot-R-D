package com.example.restfull_web_services.services;

import com.example.restfull_web_services.dtos.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Integer id);

    List<UserDto> getAllUsers();

    UserDto updateUser(Integer id, UserDto userDtoDetails);

    void deleteUser(Integer id);
}
