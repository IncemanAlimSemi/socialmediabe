package com.alseinn.socialmedia.request.follow;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowRequest {
    @NonNull
    private String followingUser;

    @NonNull
    private String followedUser;
}
