package com.alseinn.socialmedia.service.auth;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.request.auth.AuthenticationRequest;
import com.alseinn.socialmedia.request.auth.RegisterRequest;
import com.alseinn.socialmedia.response.auth.AuthenticationResponse;
import com.alseinn.socialmedia.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ResponseUtils responseUtils;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<List<User>> dbUsers = userRepository.findByUsernameOrEmailOrMobileNumber(request.getUsername(), request.getEmail(), request.getMobileNumber());

        if (dbUsers.isPresent()) {
            return AuthenticationResponse.builder()
                    .isSuccess(false)
                    .message(createRegisterMessage(request, dbUsers.get()))
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
                .isSuccess(true)
                .message(responseUtils.getMessage("registration.success.message"))
                .token(jwtToken)
                .build();
    }

    private String createRegisterMessage(RegisterRequest request, List<User> dbUsers) {
        List<String> listOfMessage = new ArrayList<>();
        for (User user: dbUsers) {
            if (user.getUsername().equals(request.getUsername())) {
                listOfMessage.add(responseUtils.getMessage("username.duplicate.message"));
            }

            if (user.getEmail().equals(request.getEmail())) {
                listOfMessage.add((responseUtils.getMessage("email.duplicate.message")));

            }

            if (user.getMobileNumber().equals(request.getMobileNumber())) {
                listOfMessage.add((responseUtils.getMessage("phone.duplicate.message")));
            }
        }

        return String.join("/", listOfMessage);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = getUserFromUsernameOrEmail(request.getCredential());

        if (Objects.isNull(user)) {
            return AuthenticationResponse.builder()
                    .isSuccess(null)
                    .message(responseUtils.getMessage("invalid.username.or.password"))
                    .token(null)
                    .build();
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), request.getPassword());

        authenticationManager.authenticate(auth);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .isSuccess(true)
                .message(responseUtils.getMessage("successfully.logged.in"))
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
