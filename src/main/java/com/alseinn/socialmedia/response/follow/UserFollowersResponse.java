package com.alseinn.socialmedia.response.follow;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowersResponse extends GeneralInformationResponse {
    private Set<FollowDataResponse> followers;
}
