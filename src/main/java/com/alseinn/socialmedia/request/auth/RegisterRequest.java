package com.alseinn.socialmedia.request.auth;

import com.alseinn.socialmedia.entity.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "{firstname.can.not.be.empty}")
    @Pattern(regexp = "^(?!.*\\d)[a-zA-Z](?:[a-zA-Z ]{1,28}[a-zA-Z])?$", message = "{firstname.validation.message}")
    private String firstname;
    @NotBlank(message = "{lastname.can.not.be.empty}")
    @Pattern(regexp = "^(?!.*\\d)[a-zA-Z](?:[a-zA-Z ]{1,28}[a-zA-Z])?$", message = "{lastname.validation.message}")
    private String lastname;
    @NotBlank(message = "{username.can.not.be.empty}")
    @Pattern(regexp = "^[a-zA-Z0-9-][a-zA-Z0-9.-]{2,14}$", message = "{username.validation.message}")
    private String username;
    @NotBlank(message = "{email.can.not.be.empty}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{email.validation.message}")
    private String email;
    @NotBlank(message = "{mobile.number.can.not.be.empty}")
    @Pattern(regexp = "^(5(0[5-7]|[3-5][0-9]))[0-9]{7}$", message = "{mobile.number.validation.message}")
    private String mobileNumber;
    @NotBlank(message = "{password.can.not.be.empty}")
    @Pattern(regexp = "^(?=\\S*[A-Z])(?=\\S*[a-z])(?=\\S*\\d).{8,}$", message = "{password.validation.message}")
    private String password;
    @NotNull(message = "{gender.can.not.be.empty}")
    private Gender gender;
}
