package com.alseinn.socialmedia.response.user;

import com.alseinn.socialmedia.entity.user.enums.Role;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
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

    @Builder(builderMethodName = "userDetailResponseBuilder")
    public UserDetailResponse(Boolean isSuccess, String message, String username, String firstname, String lastname, String email, String mobileNumber, byte[] profileImage, Role role) {
        super(isSuccess, message);
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.profileImage = profileImage;
        this.role = role;
    }
}
