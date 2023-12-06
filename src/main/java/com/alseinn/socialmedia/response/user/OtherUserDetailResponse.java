package com.alseinn.socialmedia.response.user;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OtherUserDetailResponse extends GeneralInformationResponse {
    private String username;
    private String firstname;
    private String lastname;
    private byte[] profileImage;
}
