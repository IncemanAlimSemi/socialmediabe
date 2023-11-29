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
    @NotBlank(message = "{username.can.not.be.empty}")
    @Pattern(regexp = "^[a-zA-Z0-9-][a-zA-Z0-9.-]{2,14}$", message = "{invalid.username.or.password}")
    private String username;
    @NotBlank(message = "{password.can.not.be.empty}")
    @Pattern(regexp = "^(?=\\S*[A-Z])(?=\\S*[a-z])(?=\\S*\\d).{8,}$", message = "{invalid.username.or.password}")
    private String password;
}
