package com.alseinn.socialmedia.service.follow.impl;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.request.follow.UnfollowRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.follow.FollowService;
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

    private static final Logger LOG = Logger.getLogger(FollowServiceImpl.class.getName());

    @Override
    public GeneralInformationResponse follow(FollowRequest followRequest) throws JsonProcessingException {
        User follower = userUtils.getUserFromSecurityContext();
        User followed = userRepository.findByUsername(followRequest.getFollow()).orElse(null);
        if (Objects.nonNull(follower) && Objects.nonNull(followed)) {
            if (checkIsSelf(follower.getUsername(), followed.getUsername())) {
                LOG.warning(responseUtils.getMessage("you.can.not.follow.yourself") + ": " + mapper.writeValueAsString(followRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("you.can.not.follow.yourself"));
            } else if (checkIsFollowExist(follower.getId(), followed.getId())) {
                LOG.warning(responseUtils.getMessage("you.already.follow.this.user") + ": " + mapper.writeValueAsString(followRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("you.already.follow.this.user"));
            } else {
                try {
                    follow(follower, followed);
                    userRepository.save(follower);
                    LOG.info(responseUtils.getMessage("followed.successfully") + ": " + mapper.writeValueAsString(followRequest));
                    return responseUtils.createGeneralInformationResponse(true, responseUtils.getMessage("followed.successfully"));
                } catch (Exception e) {
                    LOG.warning(responseUtils.getMessage("error.occurred.while.following") + ": " + mapper.writeValueAsString(followRequest));
                    return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("error.occurred.while.following"));
                }
            }
        }
        LOG.warning(responseUtils.getMessage("there.is.no.user.or.the.follow.field.is.empty") + ": " + mapper.writeValueAsString(followRequest));
        return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("there.is.no.user.or.the.follow.field.is.empty"));
    }

    @Override
    public GeneralInformationResponse unfollow(UnfollowRequest unfollowRequest) throws JsonProcessingException {
        User follower = userUtils.getUserFromSecurityContext();
        User followed = userRepository.findByUsername(unfollowRequest.getUnfollow()).orElse(null);
        if (Objects.nonNull(follower) && Objects.nonNull(followed)) {
            if (checkIsSelf(follower.getUsername(), followed.getUsername())) {
                LOG.warning(responseUtils.getMessage("you.can.not.unfollow.yourself")  + ": " + mapper.writeValueAsString(unfollowRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("you.can.not.unfollow.yourself"));
            } else if (!checkIsFollowExist(follower.getId(), followed.getId())) {
                LOG.warning(responseUtils.getMessage("you.can.not.unfollow.this.user") + ": " + mapper.writeValueAsString(unfollowRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("you.can.not.unfollow.this.user"));
            } else {
                try {
                    unfollow(follower, followed);
                    userRepository.save(follower);
                    LOG.info(responseUtils.getMessage("unfollowed.successfully") + ": " + mapper.writeValueAsString(unfollowRequest));
                    return responseUtils.createGeneralInformationResponse(true, responseUtils.getMessage("unfollowed.successfully"));
                } catch (Exception e) {
                    LOG.warning(responseUtils.getMessage("error.occurred.while.unfollowing") + ": " + mapper.writeValueAsString(unfollowRequest));
                    return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("error.occurred.while.unfollowing"));
                }
            }

        }
        LOG.warning(responseUtils.getMessage("there.is.no.user.or.the.unfollow.field.is.empty") + ": " + mapper.writeValueAsString(unfollowRequest));
        return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("there.is.no.user.or.the.unfollow.field.is.empty"));
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
