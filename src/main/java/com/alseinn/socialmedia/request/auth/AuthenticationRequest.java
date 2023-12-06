package com.alseinn.socialmedia.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "{credential.can.not.be.empty}")
    private String credential;
    @NotBlank(message = "{password.can.not.be.empty}")
    @Pattern(regexp = "^(?=\\S*[A-Z])(?=\\S*[a-z])(?=\\S*\\d).{8,}$", message = "{invalid.username.or.password}")
    private String password;
}