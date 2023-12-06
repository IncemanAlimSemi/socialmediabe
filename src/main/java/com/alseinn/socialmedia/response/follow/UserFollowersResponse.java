package com.alseinn.socialmedia.response.follow;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowersResponse extends GeneralInformationResponse {
    private Set<FollowDataResponse> followers;

    @Builder(builderMethodName = "userFollowersResponseBuilder")
    @SuppressWarnings(value = "unused")
    public UserFollowersResponse(Boolean isSuccess, String message,  Set<FollowDataResponse> followers) {
        super(isSuccess, message);
        this.followers = followers;
    }
}
