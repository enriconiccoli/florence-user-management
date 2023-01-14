package it.florence.assignment.user_management.service;

import it.florence.assignment.user_management.entities.User;
import it.florence.assignment.user_management.model.UserDTO;
import it.florence.assignment.user_management.repository.UserRepository;
import it.florence.assignment.user_management.exceptions.NotFoundException;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public List<UserDTO> findByNameSurname(String name, String surname){

        if(!StringUtils.hasLength(surname) && !StringUtils.hasLength(name)){
            return Collections.emptyList();
        }

        final List<User> users = userRepository.findUsersByNameSurname(name, surname);
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public Long create(UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(Long id, UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(User user, UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setMail(user.getMail());
        userDTO.setAddress(user.getAddress());
        return userDTO;
    }

    private User mapToEntity(UserDTO userDTO, User user) {
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setMail(userDTO.getMail());
        user.setAddress(userDTO.getAddress());

        return user;
    }

}
