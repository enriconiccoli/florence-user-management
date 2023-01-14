package it.florence.assignment.user_management.repository;

import it.florence.assignment.user_management.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByNameSurname(String name, String surname);
}
