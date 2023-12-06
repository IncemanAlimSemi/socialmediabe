package com.alseinn.socialmedia.response.follow;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowingsResponse extends GeneralInformationResponse {
    private Set<FollowDataResponse> followings;

    @Builder(builderMethodName = "userFollowingsResponseBuilder")
    @SuppressWarnings(value = "unused")
    public UserFollowingsResponse(Boolean isSuccess, String message,  Set<FollowDataResponse> followings) {
        super(isSuccess, message);
        this.followings = followings;
    }
}
