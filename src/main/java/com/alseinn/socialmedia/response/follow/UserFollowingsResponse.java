package com.alseinn.socialmedia.response.follow;

import com.alseinn.socialmedia.response.follow.FollowDataResponse;
import com.alseinn.socialmedia.response.concrete.AbstractResponse;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowingsResponse extends AbstractResponse {
    private boolean isSuccess;
    private Set<FollowDataResponse> followings;
}
