package com.alseinn.socialmedia.service.auth;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.request.auth.AuthenticationRequest;
import com.alseinn.socialmedia.request.auth.RegisterRequest;
import com.alseinn.socialmedia.response.auth.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> dbUser = userRepository.findByUsername(request.getUsername());

        if (dbUser.isPresent()) {
            return AuthenticationResponse.builder()
                    .token(null)
                    .build();
        }

        User user =  User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .gender(request.getGender())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .timeCreated(new Date(System.currentTimeMillis()))
                .timeModified(new Date(System.currentTimeMillis()))
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = getUserFromUsernameOrEmail(request.getCredential());

        if (Objects.isNull(user)) {
            return AuthenticationResponse.builder()
                    .token(null)
                    .build();
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), request.getPassword());

        authenticationManager.authenticate(auth);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private User getUserFromUsernameOrEmail(String credential) {
        if (credential.contains("@")) {
            return userRepository.findByEmail(credential).orElse(null);
        } else {
            return userRepository.findByUsername(credential).orElse(null);
        }
    }
}
