package com.alseinn.socialmedia.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowingsResponse {
    private boolean isSuccess;
    private Set<FollowDataResponse> followings;
}
