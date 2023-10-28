package com.alseinn.socialmedia.service.user;

import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.response.user.UserFollowersResponse;
import com.alseinn.socialmedia.response.user.UserFollowingsResponse;

import java.util.Set;

public interface UserService {
    User findByUsername(String username);

    UserFollowersResponse getFollowers(String username);

    UserFollowingsResponse getFollowings(String username);
}
