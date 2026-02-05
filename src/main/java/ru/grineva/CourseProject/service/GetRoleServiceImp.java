package ru.grineva.CourseProject.service;

import org.springframework.stereotype.Service;
import ru.grineva.CourseProject.dto.UserDto;

@Service
public class GetRoleServiceImp implements GetRoleService{
    private final UserService userService;
    public GetRoleServiceImp(UserService userService)
    {
        this.userService = userService;
    }
    @Override
    public String getRoleCurrentUser() {
        UserDto userDto = userService.getCurrentUser();
        return userDto.getRole();
    }
}
