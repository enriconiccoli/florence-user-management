package it.florence.assignment.service;

import io.quarkus.runtime.util.StringUtil;
import it.florence.assignment.entity.UserEntity;
import it.florence.assignment.model.MultipartBody;
import it.florence.assignment.model.UserDTO;
import it.florence.assignment.utils.CSVHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ApplicationScoped
public class UserService {


    public List<UserDTO> findAll() {
        final List<UserEntity> users = UserEntity.findAll().list();
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(Long id) {

        UserEntity user = UserEntity.findById(id);

        if(user != null){
            return mapToDTO(user, new UserDTO());
        }

        throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
    }

    public List<UserDTO> findByNameSurname(String name, String surname){

        if(StringUtil.isNullOrEmpty(surname) && StringUtil.isNullOrEmpty(name)){
            return Collections.emptyList();
        }

        final List<UserEntity> users = UserEntity.findUsersByNameSurname(name, surname);
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    @Transactional
    public void create(UserDTO userDTO) {

        final UserEntity user = mapToEntity(userDTO, new UserEntity());
        user.persist();
    }

    @Transactional
    public void saveFromCSV(MultipartBody data) {
        List<UserDTO> userDTOList = CSVHelper.csvToUserDTO(data.getFile());

        List<UserEntity> userList = new ArrayList<>();
        for (UserDTO userDTO : userDTOList) {

            if(userDTO.getId() != null){
                UserEntity user = UserEntity.findById(userDTO.getId());
                if(user != null){
                    userList.add(mapToEntity(userDTO, user));
                }
            }
            else{
                userList.add(mapToEntity(userDTO, new UserEntity()));
            }

        }
        UserEntity.persist(userList);
    }

    @Transactional
    public void update(Long id, UserDTO userDTO) {

        UserEntity user = UserEntity.findById(id);

        if(user == null) {
            throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
        }

        mapToEntity(userDTO, user);
        user.persist(user);
    }

    @Transactional
    public void delete(Long id) {
        UserEntity.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        UserEntity.deleteAll();
    }

    private UserDTO mapToDTO(UserEntity user, UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setMail(user.getMail());
        userDTO.setAddress(user.getAddress());
        return userDTO;
    }

    private UserEntity mapToEntity(UserDTO userDTO, UserEntity user) {

        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setMail(userDTO.getMail());
        user.setAddress(userDTO.getAddress());

        return user;
    }

}
