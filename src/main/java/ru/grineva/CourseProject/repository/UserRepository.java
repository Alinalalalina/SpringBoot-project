package ru.grineva.CourseProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.grineva.CourseProject.entity.Author;


public interface UserRepository extends JpaRepository<Author, Long> {
    Author findByEmail(String email);
}