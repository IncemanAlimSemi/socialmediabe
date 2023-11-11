package com.alseinn.socialmedia.request.auth;

import com.alseinn.socialmedia.entity.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String mobileNumber;
    private String password;
    private Gender gender;
    private MultipartFile profileImage;
}
