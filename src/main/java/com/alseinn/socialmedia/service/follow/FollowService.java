package com.alseinn.socialmedia.service.follow;

import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.request.follow.UnfollowRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface FollowService {

    GeneralInformationResponse follow(FollowRequest followRequest) throws JsonProcessingException;

    GeneralInformationResponse unfollow(UnfollowRequest followRequest) throws JsonProcessingException;

}
