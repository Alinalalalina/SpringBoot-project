package ru.grineva.CourseProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.grineva.CourseProject.entity.UserActions;

public interface UserActionsRepository extends JpaRepository<UserActions, Long> {

}
