package ru.grineva.CourseProject.service;


import ru.grineva.CourseProject.dto.UserDto;
import ru.grineva.CourseProject.entity.Author;

import java.util.List;

public interface UserService {
    void saveUserRole(UserDto userDTO);
    void saveUser(UserDto userDto);

    UserDto getCurrentUser();
    Author findUserByEmail(String email);
    List<UserDto> findAllUsers();

    UserDto mapToUserDto(Author user);
}