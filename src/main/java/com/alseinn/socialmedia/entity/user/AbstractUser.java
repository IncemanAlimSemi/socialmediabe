package com.alseinn.socialmedia.entity.user;

import com.alseinn.socialmedia.entity.item.Item;
import com.alseinn.socialmedia.entity.user.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractUser extends Item {
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}