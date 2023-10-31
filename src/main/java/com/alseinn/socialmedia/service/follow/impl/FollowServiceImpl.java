package com.alseinn.socialmedia.service.follow.impl;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.request.follow.UnfollowRequest;
import com.alseinn.socialmedia.response.follow.FollowResponse;
import com.alseinn.socialmedia.service.follow.FollowService;
import com.alseinn.socialmedia.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final UserUtils userUtils;

    @Override
    public FollowResponse follow(FollowRequest followRequest) {
        User follower = userUtils.getUserFromSecurityContext(); //takipçi
        User followed = userRepository.findByUsername(followRequest.getFollow()).orElse(null); //takip eden
        if (checkIsSelf(follower.getUsername(), followed.getUsername())) {
            return createFollowResponse("You can't follow yourself", false);
        } else if (checkIsFollowExist(follower.getId(), followed.getId())) {
            return createFollowResponse("You already follow this user", false);
        } else if (Objects.nonNull(follower) && Objects.nonNull(followed)) {
            try {
                follow(follower, followed);
                userRepository.save(follower);
                return createFollowResponse("Followed successfully", true);
            } catch (Exception e) {
                return createFollowResponse("Error occurred while following", false);
            }

        }
        return createFollowResponse("There is no user or the follow field is empty", false);
    }

    @Override
    public FollowResponse unfollow(UnfollowRequest followRequest) {
        User follower = userUtils.getUserFromSecurityContext(); //takipçi
        User followed = userRepository.findByUsername(followRequest.getUnfollow()).orElse(null); //takip eden
        if (checkIsSelf(follower.getUsername(), followed.getUsername())) {
            return createFollowResponse("You can't unfollow yourself", false);
        } else if (!checkIsFollowExist(follower.getId(), followed.getId())) {
            return createFollowResponse("You don't unfollow this user", false);
        } else if (Objects.nonNull(follower) && Objects.nonNull(followed)) {
            try {
                unfollow(follower, followed);
                userRepository.save(follower);
                return createFollowResponse("Unfollowed successfully", true);
            } catch (Exception e) {
                return createFollowResponse("Error occurred while unfollowing", false);
            }

        }
        return createFollowResponse("There is no user or the unfollow field is empty", false);
    }

    private void follow(User user, User userToFollow) {
        user.getFollowings().add(userToFollow);
        userToFollow.getFollowers().add(user);
    }

    private void unfollow(User user, User userToUnfollow) {
        user.getFollowings().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(user);
    }

    private FollowResponse createFollowResponse(String message, Boolean isSuccess) {
        return FollowResponse.builder()
                .message(message)
                .isSuccess(isSuccess)
                .build();
    }

    public Boolean checkIsFollowExist(Long userId, Long followId) {
        return userRepository.checkIsFollowExist(userId, followId);
    }

    public Boolean checkIsSelf(String user, String follow) {
        return user.equals(follow);
    }


}
