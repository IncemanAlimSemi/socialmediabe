package com.alseinn.socialmedia.controller.auth;

import com.alseinn.socialmedia.request.auth.AuthenticationRequest;
import com.alseinn.socialmedia.request.auth.RegisterRequest;
import com.alseinn.socialmedia.response.auth.AuthenticationResponse;
import com.alseinn.socialmedia.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${default.api.path}/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
