package com.alseinn.socialmedia.service.follow.impl;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.request.follow.UnfollowRequest;
import com.alseinn.socialmedia.response.follow.FollowResponse;
import com.alseinn.socialmedia.service.follow.FollowService;
import com.alseinn.socialmedia.service.post.impl.PostServiceImpl;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final ObjectMapper mapper;
    private static final Logger LOG = Logger.getLogger(PostServiceImpl.class.getName());

    @Override
    public FollowResponse follow(FollowRequest followRequest) throws JsonProcessingException {
        User follower = userUtils.getUserFromSecurityContext(); //takipçi
        User followed = userRepository.findByUsername(followRequest.getFollow()).orElse(null); //takip eden
        if (Objects.nonNull(follower) && Objects.nonNull(followed)) {
            if (checkIsSelf(follower.getUsername(), followed.getUsername())) {
                LOG.warning("You can't follow yourself: " + mapper.writeValueAsString(followRequest));
                return createFollowResponse("You can't follow yourself", false);
            } else if (checkIsFollowExist(follower.getId(), followed.getId())) {
                return createFollowResponse("You already follow this user", false);
            } else {
                try {
                    follow(follower, followed);
                    userRepository.save(follower);
                    return createFollowResponse("Followed successfully", true);
                } catch (Exception e) {
                    return createFollowResponse("Error occurred while following", false);
                }
            }

        }
        LOG.warning("There is no user or the follow field is empty: " + mapper.writeValueAsString(followRequest));
        return createFollowResponse("There is no user or the follow field is empty", false);
    }

    @Override
    public FollowResponse unfollow(UnfollowRequest unfollowRequest) throws JsonProcessingException {
        User follower = userUtils.getUserFromSecurityContext(); //takipçi
        User followed = userRepository.findByUsername(unfollowRequest.getUnfollow()).orElse(null); //takip eden
        if (Objects.nonNull(follower) && Objects.nonNull(followed)) {
            if (checkIsSelf(follower.getUsername(), followed.getUsername())) {
                LOG.warning("You can't unfollow yourself: " + mapper.writeValueAsString(unfollowRequest));

                return createFollowResponse("You can't unfollow yourself", false);
            } else if (!checkIsFollowExist(follower.getId(), followed.getId())) {
                LOG.warning("You don't unfollow this user: " + mapper.writeValueAsString(unfollowRequest));

                return createFollowResponse("You don't unfollow this user", false);
            } else {
                try {
                    unfollow(follower, followed);
                    userRepository.save(follower);
                    return createFollowResponse("Unfollowed successfully", true);
                } catch (Exception e) {
                    return createFollowResponse("Error occurred while unfollowing", false);
                }
            }

        }
        LOG.warning("There is no user or the unfollow field is empty: " + mapper.writeValueAsString(unfollowRequest));
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
