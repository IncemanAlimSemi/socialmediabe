package com.alseinn.socialmedia.service.user.impl;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.image.Image;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.image.UploadImageRequest;
import com.alseinn.socialmedia.response.follow.FollowDataResponse;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.response.image.ImageResponse;
import com.alseinn.socialmedia.response.user.OtherUserDetailResponse;
import com.alseinn.socialmedia.response.user.UserDetailResponse;
import com.alseinn.socialmedia.response.follow.UserFollowersResponse;
import com.alseinn.socialmedia.response.follow.UserFollowingsResponse;
import com.alseinn.socialmedia.service.storage.ImageService;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.ResponseUtils;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.alseinn.socialmedia.utils.contants.AppTRConstants.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectMapper mapper;
    private final UserUtils userUtils;
    private final ImageService imageService;
    private final ResponseUtils responseUtils;

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public GeneralInformationResponse getProfile(String username) throws IOException {
        User user = findByUsername(username);

        if (Objects.nonNull(user)) {

            if (userUtils.isSessionUser(user)) {
                LOG.info(responseUtils.getMessage("listed.with.success", USER));
                return UserDetailResponse.builder()
                        .isSuccess(true)
                        .message(responseUtils.getMessage("listed.with.success", USER))
                        .username(user.getUsername())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .email(user.getEmail())
                        .mobileNumber(user.getMobileNumber())
                        .profileImage(Objects.nonNull(user.getProfileImage()) ? imageService.getImage(user.getProfileImage().getId()) : null)
                        .role(user.getRole())
                        .build();
            }

            LOG.warning(responseUtils.getMessage("this.user.is.not.session.user") + " -- User: " + mapper.writeValueAsString(user));
            return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("this.user.is.not.session.user"));
        }

        LOG.warning(responseUtils.getMessage("not.found", USER) + " -- Username: " + username);
        return responseUtils.createGeneralInformationResponse(false,
                responseUtils.getMessage("user.not.found"));


    }

    @Override
    public GeneralInformationResponse getOtherProfile(String username) throws IOException {
        User user = findByUsername(username);
        if (Objects.nonNull(user)) {
            LOG.info(responseUtils.getMessage("listed.with.success", USER) + "-- Username" + mapper.writeValueAsString(username));
            return OtherUserDetailResponse.builder()
                    .isSuccess(true)
                    .message(responseUtils.getMessage("listed.with.success", USER))
                    .username(user.getUsername())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .profileImage(Objects.nonNull(user.getProfileImage()) ? imageService.getImage(user.getProfileImage().getId()) : null)
                    .build();
        }

        LOG.warning(responseUtils.getMessage("user.not.found") + " -- Username: " + username);
        return responseUtils.createGeneralInformationResponse(false,
                responseUtils.getMessage("user.not.found"));
    }

    @Override
    public UserFollowersResponse getFollowers(String username) throws JsonProcessingException {
        User user = findByUsername(username);
        if (Objects.nonNull(user)) {
            return getUserFollowersResponse(user);
        }
        LOG.warning(responseUtils.getMessage("can.not.be.null", USER) + " -- Username: " + mapper.writeValueAsString(username));
        return createUserFollowersResponse(false, responseUtils.getMessage("user.not.found"), new HashSet<>(0));

    }

    @Override
    public UserFollowingsResponse getFollowings(String username) throws JsonProcessingException {
        User user = findByUsername(username);
        if (Objects.nonNull(user)) {
            return getUserFollowingsResponse(user);
        }
        LOG.warning(responseUtils.getMessage("user.not.found") + " -- Username: " + mapper.writeValueAsString(username));
        return createUserFollowingsResponse(false, responseUtils.getMessage("user.not.found"), new HashSet<>(0));

    }

    @Transactional
    @Override
    public GeneralInformationResponse updateProfilePicture(UploadImageRequest uploadImageRequest) {
        User user = userUtils.getUserFromSecurityContext();
        if (Objects.nonNull(user)) {
            try {

                if (Objects.nonNull(user.getProfileImage())) {
                    if (!removeProfilePicture().getIsSuccess()) {
                        LOG.warning(responseUtils.getMessage("could.not.be.removed", PROFILE_PICTURE));
                        return responseUtils.createGeneralInformationResponse(false,
                                responseUtils.getMessage("could.not.be.removed", PROFILE_PICTURE));
                    }
                    LOG.info(responseUtils.getMessage("removed.with.success", PROFILE_PICTURE));
                }

                ImageResponse imageResponse = imageService.uploadImage(uploadImageRequest.getImage());
                if (imageResponse.getIsSuccess()){
                    user.setProfileImage(imageResponse.getImage());
                    userRepository.save(user);
                    LOG.info(MessageFormat.format(responseUtils.getMessage("saved.with.success", IMAGE) + ": {0} - {1} - {2}"
                            , user.getProfileImage().getId(), user.getProfileImage().getName(), user.getProfileImage().getType()));
                    return responseUtils.createGeneralInformationResponse(true,
                            responseUtils.getMessage("saved.with.success", IMAGE));
                }

                LOG.warning(responseUtils.getMessage("could.not.be.saved", PROFILE_PICTURE));
                return responseUtils.createGeneralInformationResponse(false, imageResponse.getMessage());


            } catch (Exception e) {
                LOG.warning("Error occurred while saving profile picture: " + e);
                return responseUtils.createGeneralInformationResponse(false,
                        responseUtils.getMessage("could.not.be.saved", PROFILE_PICTURE));
            }

        }

        LOG.warning(responseUtils.getMessage("user.not.found"));
        return responseUtils.createGeneralInformationResponse(false,
                responseUtils.getMessage("user.not.found"));

    }

    @Transactional
    @Override
    public GeneralInformationResponse removeProfilePicture() {
        User user = userUtils.getUserFromSecurityContext();

        if (Objects.nonNull(user)) {
            try {
                if (Objects.nonNull(user.getProfileImage())) {
                    removeProfileImage(user);
                    return responseUtils.createGeneralInformationResponse(true,
                            responseUtils.getMessage("removed.with.success", PROFILE_PICTURE));
                }
                LOG.warning("Profile picture not exist.");
                return responseUtils.createGeneralInformationResponse(false,
                        responseUtils.getMessage("not.exist", PROFILE_PICTURE));
            } catch (Exception e) {
                LOG.warning("Error occurred while removing profile picture: " + e);

                return responseUtils.createGeneralInformationResponse(false,
                        responseUtils.getMessage("could.not.be.removed", PROFILE_PICTURE));
            }
        }

        LOG.warning(responseUtils.getMessage("user.not.found"));
        return responseUtils.createGeneralInformationResponse(false,
                responseUtils.getMessage("user.not.found"));
    }

    private UserFollowersResponse getUserFollowersResponse(User user) {
        Set<FollowDataResponse> userFollowersDataResponses = user.getFollowers().stream().map(follower -> FollowDataResponse.builder()
                .username(follower.getUsername())
                .build()).collect(Collectors.toSet());

        return createUserFollowersResponse(true, responseUtils.getMessage("listed.with.success", FOLLOWERS), userFollowersDataResponses);
    }

    private UserFollowingsResponse getUserFollowingsResponse(User user) {
        Set<FollowDataResponse> userFollowingsDataResponses = user.getFollowings().stream().map(following -> FollowDataResponse.builder()
                .username(following.getUsername())
                .build()).collect(Collectors.toSet());

        return createUserFollowingsResponse(true, responseUtils.getMessage("listed.with.success", FOLLOWINGS), userFollowingsDataResponses);
    }

    public UserFollowersResponse createUserFollowersResponse(Boolean isSuccess, String message, Set<FollowDataResponse> followers) {
        return UserFollowersResponse.builder()
                .message(message)
                .followers(followers)
                .isSuccess(isSuccess)
                .build();
    }

    public UserFollowingsResponse createUserFollowingsResponse(Boolean isSuccess, String message, Set<FollowDataResponse> followings) {
        return UserFollowingsResponse.builder()
                .message(message)
                .followings(followings)
                .isSuccess(isSuccess)
                .build();
    }

    private void removeProfileImage(User user) throws IOException {
        Image profileImage = user.getProfileImage();

        if (Objects.nonNull(profileImage)) {
            user.setProfileImage(null);
            imageService.deleteImage(profileImage);
            userRepository.save(user);
        }
        LOG.warning(responseUtils.getMessage("not.exist", PROFILE_PICTURE));
    }

}
