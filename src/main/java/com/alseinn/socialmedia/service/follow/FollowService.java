package com.alseinn.socialmedia.service.follow;

import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.request.follow.UnfollowRequest;
import com.alseinn.socialmedia.response.follow.FollowResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface FollowService {

    FollowResponse follow(FollowRequest followRequest) throws JsonProcessingException;

    FollowResponse unfollow(UnfollowRequest followRequest) throws JsonProcessingException;

}
