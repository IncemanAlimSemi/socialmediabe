package com.alseinn.socialmedia.service.user;

import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.response.user.UserFollowersResponse;
import com.alseinn.socialmedia.response.user.UserFollowingsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {
    User findByUsername(String username);

    UserFollowersResponse getFollowers(String username) throws JsonProcessingException;

    UserFollowingsResponse getFollowings(String username) throws JsonProcessingException;
}
