package com.alseinn.socialmedia.response.user;

import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.response.GeneralResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse extends GeneralResponse {

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private String mobileNumber;

    private byte[] profileImage;

    private Role role;
}
