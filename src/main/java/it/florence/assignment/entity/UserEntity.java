package it.florence.assignment.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@NamedQuery(name = "UserEntity.findUsersByNameSurname", query = "from UserEntity u where u.name = ?1 and u.surname = ?2")
public class UserEntity extends PanacheEntityBase {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 1000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String mail;

    @Column
    private String address;

    public static List<UserEntity> findUsersByNameSurname(String name, String surname) {
        return find("#UserEntity.findUsersByNameSurname", name, surname).list();
    }
}
