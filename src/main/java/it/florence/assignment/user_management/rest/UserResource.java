package it.florence.assignment.user_management.rest;

import it.florence.assignment.user_management.model.UserDTO;
import it.florence.assignment.user_management.service.UserService;
import it.florence.assignment.user_management.utils.CSVHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping("/getByName")
    public ResponseEntity<List<UserDTO>> findUsersByNameSurname(@RequestParam(value = "name",required = false) @Valid String name, @RequestParam(value = "surname",required = false) @Valid String surname) {

        return ResponseEntity.ok(userService.findByNameSurname(name,surname));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/csv")
    public ResponseEntity<Void> uploadCSV(@RequestParam("file") MultipartFile file) {

        if (CSVHelper.hasCSVFormat(file)) {
                userService.saveFromCSV(file);
                return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id,
            @RequestBody @Valid UserDTO userDTO) {
        userService.update(id, userDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll(){
        userService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
