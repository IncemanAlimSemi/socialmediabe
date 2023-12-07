package com.alseinn.socialmedia.response.auth;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse extends GeneralInformationResponse {
    private String token;
}
