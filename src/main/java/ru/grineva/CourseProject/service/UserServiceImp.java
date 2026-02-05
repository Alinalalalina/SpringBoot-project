package ru.grineva.CourseProject.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.grineva.CourseProject.dto.UserDto;
import ru.grineva.CourseProject.entity.Role;
import ru.grineva.CourseProject.entity.Author;
import ru.grineva.CourseProject.repository.RoleRepository;
import ru.grineva.CourseProject.repository.UserRepository;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUserRole(UserDto userDTO) {

        Author user = findUserByEmail(userDTO.getEmail());
        Role role = checkRoleExist(userDTO.getRole());
        role = roleRepository.findById(role.getId()).get();
        user.getRoles().remove(0);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public void saveUser(UserDto userDto) {
        Author user = new Author();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName(userDto.getRole());
        if (role == null) {
            role = checkRoleExist(userDto.getRole());
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Author user = userRepository.findByEmail(authentication.getName());
        UserDto currUser = mapToUserDto(user);
        return currUser;
    }

    @Override
    public Author findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<Author> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto mapToUserDto(Author user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRoles().get(0).getName());
        int countOfSongs = user.getSongs().size();
        userDto.setCountOfSongs(countOfSongs);
        return userDto;
    }

    private Role checkRoleExist(String roleString) {
        Role role = new Role();
        role.setName(roleString);
        if(roleRepository.findByName(roleString)==null)
            return roleRepository.save(role);
        return roleRepository.findByName(roleString);
    }

}