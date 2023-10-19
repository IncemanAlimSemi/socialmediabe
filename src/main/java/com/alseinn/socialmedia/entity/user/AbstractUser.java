package com.alseinn.socialmedia.entity.user;

import com.alseinn.socialmedia.entity.user.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public AbstractUser(String firstname, String lastname, Gender gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
    }
}