package com.alseinn.socialmedia.entity.user;

import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.enums.Gender;
import com.alseinn.socialmedia.entity.user.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends AbstractUser implements UserDetails {

    @Column(nullable = false ,unique = true)
    private String email;

    @Column(nullable = false ,unique = true)
    private String mobileNumber;

    @Column(nullable = false ,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Post> posts;

    @Builder
    public User(String firstname, String lastname, Gender gender, String email, String mobileNumber, String username, String password, Role role) {
        super(firstname, lastname, gender);
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
