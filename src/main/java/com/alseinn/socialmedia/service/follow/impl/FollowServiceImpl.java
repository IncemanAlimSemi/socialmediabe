package com.alseinn.socialmedia.service.follow.impl;

import com.alseinn.socialmedia.dao.follow.FollowRepository;
import com.alseinn.socialmedia.entity.follow.Follow;
import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.response.follow.FollowResponse;
import com.alseinn.socialmedia.service.follow.FollowService;
import com.alseinn.socialmedia.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Override
    public FollowResponse follow(FollowRequest followRequest) {
        final String username = followRequest.getFollowingUser();
        final String targetUsername = followRequest.getFollowedUser();
        final Follow checkFollow = followRepository.getByFollowerUsernameAndFollowingUsername(username, targetUsername);
        if (Objects.isNull(checkFollow)) {
            try {
                Follow follow = Follow.builder()
                        .follower(userService.findByUsername(username))
                        .following(userService.findByUsername(targetUsername))
                        .build();
                followRepository.save(follow);
                return FollowResponse.builder()
                        .isSuccess(true)
                        .message("Followed successfully")
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Something went wrong: " + e.getMessage());
            }
        }
        return FollowResponse.builder()
                .isSuccess(false)
                .message("Already following")
                .build();
    }

    @Override
    public FollowResponse unfollow(FollowRequest unFollowRequest) {
        final String username = unFollowRequest.getFollowingUser();
        final String targetUsername = unFollowRequest.getFollowedUser();
        final Follow checkfollow = followRepository.getByFollowerUsernameAndFollowingUsername(username, targetUsername);
        if (Objects.nonNull(checkfollow)) {
            try {
                followRepository.delete(checkfollow);
                return FollowResponse.builder()
                        .isSuccess(true)
                        .message("Unfollowed successfully")
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Something went wrong: " + e.getMessage());
            }
        }
        return FollowResponse.builder()
                .isSuccess(false)
                .message("The unfollow operation was not successful.")
                .build();

    }
}
