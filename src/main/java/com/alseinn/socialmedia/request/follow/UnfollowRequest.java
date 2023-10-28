package com.alseinn.socialmedia.request.follow;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnfollowRequest {

    @NonNull
    private String unfollow;

}
