package it.florence.assignment.user_management.model;

import jakarta.validation.constraints.Size;


public class UserDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String surname;

    @Size(max = 255)
    private String mail;

    @Size(max = 255)
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(final String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

}
