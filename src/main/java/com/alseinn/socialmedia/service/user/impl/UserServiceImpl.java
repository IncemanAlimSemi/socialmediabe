package com.alseinn.socialmedia.service.user.impl;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.response.post.PostResponse;
import com.alseinn.socialmedia.response.user.FollowDataResponse;
import com.alseinn.socialmedia.response.user.UserFollowersResponse;
import com.alseinn.socialmedia.response.user.UserFollowingsResponse;
import com.alseinn.socialmedia.service.post.impl.PostServiceImpl;
import com.alseinn.socialmedia.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectMapper mapper;
    private static final Logger LOG = Logger.getLogger(PostServiceImpl.class.getName());

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public UserFollowersResponse getFollowers(String username) throws JsonProcessingException {
        User user = findByUsername(username);
        if (Objects.nonNull(user)) {
            return getUserFollowersResponse(user);
        }
        LOG.warning("User can not be null! : " + mapper.writeValueAsString(username));
        return UserFollowersResponse.builder()
                .followers(new HashSet<>(0))
                .isSuccess(false)
                .build();

    }

    @Override
    public UserFollowingsResponse getFollowings(String username) throws JsonProcessingException {
        User user = findByUsername(username);
        if (Objects.nonNull(user)) {
            return getUserFollowingsResponse(user);
        }
        LOG.warning("User can not be null! : " + mapper.writeValueAsString(username));
        return UserFollowingsResponse.builder()
                .followings(new HashSet<>(0))
                .isSuccess(false)
                .build();
    }

    private UserFollowersResponse getUserFollowersResponse(User user) {
        Set<FollowDataResponse> userFollowersDataResponses = user.getFollowers().stream().map(follower -> FollowDataResponse.builder()
                .username(follower.getUsername())
                .build()).collect(Collectors.toSet());

        return UserFollowersResponse.builder()
                .followers(userFollowersDataResponses)
                .isSuccess(true)
                .build();
    }

    private UserFollowingsResponse getUserFollowingsResponse(User user) {
        Set<FollowDataResponse> userFollowingsDataResponses = user.getFollowings().stream().map(following -> FollowDataResponse.builder()
                .username(following.getUsername())
                .build()).collect(Collectors.toSet());

        return UserFollowingsResponse.builder()
                .followings(userFollowingsDataResponses)
                .isSuccess(true)
                .build();
    }

}
