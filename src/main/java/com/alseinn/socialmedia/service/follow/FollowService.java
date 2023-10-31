package com.alseinn.socialmedia.service.follow;

import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.request.follow.UnfollowRequest;
import com.alseinn.socialmedia.response.follow.FollowResponse;

public interface FollowService {

    FollowResponse follow(FollowRequest followRequest);

    FollowResponse unfollow(UnfollowRequest followRequest);

}
