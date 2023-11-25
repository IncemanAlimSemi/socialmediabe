package com.alseinn.socialmedia.service.follow.impl;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.request.follow.UnfollowRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.follow.FollowService;
import com.alseinn.socialmedia.service.post.impl.PostServiceImpl;
import com.alseinn.socialmedia.utils.ResponseUtils;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final ObjectMapper mapper;
    private final ResponseUtils responseUtils;

    private static final Logger LOG = Logger.getLogger(PostServiceImpl.class.getName());

    @Override
    public GeneralInformationResponse follow(FollowRequest followRequest) throws JsonProcessingException {
        User follower = userUtils.getUserFromSecurityContext();
        User followed = userRepository.findByUsername(followRequest.getFollow()).orElse(null);
        if (Objects.nonNull(follower) && Objects.nonNull(followed)) {
            if (checkIsSelf(follower.getUsername(), followed.getUsername())) {
                LOG.warning("You can't follow yourself: " + mapper.writeValueAsString(followRequest));
                return responseUtils.createGeneralInformationResponse(false, "You can't follow yourself");
            } else if (checkIsFollowExist(follower.getId(), followed.getId())) {
                return responseUtils.createGeneralInformationResponse(false, "You already follow this user");
            } else {
                try {
                    follow(follower, followed);
                    userRepository.save(follower);
                    return responseUtils.createGeneralInformationResponse(true, "Followed successfully");
                } catch (Exception e) {
                    return responseUtils.createGeneralInformationResponse(false, "Error occurred while following");
                }
            }

        }
        LOG.warning("There is no user or the follow field is empty: " + mapper.writeValueAsString(followRequest));
        return responseUtils.createGeneralInformationResponse(false, "There is no user or the follow field is empty");
    }

    @Override
    public GeneralInformationResponse unfollow(UnfollowRequest unfollowRequest) throws JsonProcessingException {
        User follower = userUtils.getUserFromSecurityContext();
        User followed = userRepository.findByUsername(unfollowRequest.getUnfollow()).orElse(null);
        if (Objects.nonNull(follower) && Objects.nonNull(followed)) {
            if (checkIsSelf(follower.getUsername(), followed.getUsername())) {
                LOG.warning("You can't unfollow yourself: " + mapper.writeValueAsString(unfollowRequest));

                return responseUtils.createGeneralInformationResponse(false, "You can't unfollow yourself");
            } else if (!checkIsFollowExist(follower.getId(), followed.getId())) {
                LOG.warning("You don't unfollow this user: " + mapper.writeValueAsString(unfollowRequest));

                return responseUtils.createGeneralInformationResponse(false, "You don't unfollow this user");
            } else {
                try {
                    unfollow(follower, followed);
                    userRepository.save(follower);
                    return responseUtils.createGeneralInformationResponse(true, "Unfollowed successfully");
                } catch (Exception e) {
                    return responseUtils.createGeneralInformationResponse(false, "Error occurred while unfollowing");
                }
            }

        }
        LOG.warning("There is no user or the unfollow field is empty: " + mapper.writeValueAsString(unfollowRequest));
        return responseUtils.createGeneralInformationResponse(false, "There is no user or the unfollow field is empty");
    }

    private void follow(User user, User userToFollow) {
        user.getFollowings().add(userToFollow);
        userToFollow.getFollowers().add(user);
    }

    private void unfollow(User user, User userToUnfollow) {
        user.getFollowings().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(user);
    }

    public Boolean checkIsFollowExist(Long userId, Long followId) {
        return userRepository.checkIsFollowExist(userId, followId);
    }

    public Boolean checkIsSelf(String user, String follow) {
        return user.equals(follow);
    }


}
