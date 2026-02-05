package ru.grineva.CourseProject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.grineva.CourseProject.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}