package com.alseinn.socialmedia.response.user;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherUserDetailResponse extends GeneralInformationResponse {

    private String username;

    private String firstname;

    private String lastname;

    private byte[] profileImage;

    @Builder(builderMethodName = "otherUserDetailResponseBuilder")
    public OtherUserDetailResponse(Boolean isSuccess, String message, String username, String firstname, String lastname, String email, String mobileNumber, byte[] profileImage) {
        super(isSuccess, message);
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.profileImage = profileImage;
    }
}
