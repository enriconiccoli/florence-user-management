package it.florence.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String name;

    private String surname;

    private String mail;

    private String address;

}
