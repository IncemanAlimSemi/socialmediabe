package com.alseinn.socialmedia.response.user;

import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse extends GeneralInformationResponse {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String mobileNumber;
    private byte[] profileImage;
    private Role role;
}
