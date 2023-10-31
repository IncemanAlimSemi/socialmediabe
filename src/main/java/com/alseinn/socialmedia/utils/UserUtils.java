package com.alseinn.socialmedia.utils;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserUtils {

    private final UserRepository userRepository;

    public UserUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserFromSecurityContext() {
        return userRepository.findByUsername(((User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername()).orElseThrow();
    }

    public boolean isSessionUser(User user) {
        String sessionUsername = getUserFromSecurityContext().getUsername();
        return Objects.equals(user.getUsername(), sessionUsername);
    }
}
