package com.alseinn.socialmedia.service.user.impl;

import com.alseinn.socialmedia.dao.user.UserRepository;
import com.alseinn.socialmedia.entity.image.Image;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.image.UploadImageRequest;
import com.alseinn.socialmedia.response.GeneralResponse;
import com.alseinn.socialmedia.response.follow.FollowDataResponse;
import com.alseinn.socialmedia.response.user.UserDetailResponse;
import com.alseinn.socialmedia.response.follow.UserFollowersResponse;
import com.alseinn.socialmedia.response.follow.UserFollowingsResponse;
import com.alseinn.socialmedia.service.post.impl.PostServiceImpl;
import com.alseinn.socialmedia.service.storage.ImageService;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    private final UserUtils userUtils;
    private final ImageService imageService;
    private static final Logger LOG = Logger.getLogger(PostServiceImpl.class.getName());

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public GeneralResponse getUserDetail(String username) throws IOException {
        User user = findByUsername(username);
        if (Objects.isNull(user)) {
            LOG.warning("User not found! : " + username);
            return GeneralResponse.builder()
                    .isSuccess(false)
                    .message("User not found!")
                    .build();
        }
        return UserDetailResponse.builder()
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .profileImage(Objects.nonNull(user.getProfileImage()) ? imageService.getImage(user.getId()) : null)
                .role(user.getRole())
                .build();
    }

    @Override
    public UserFollowersResponse getFollowers(String username) throws JsonProcessingException {
        User user = findByUsername(username);
        if (Objects.nonNull(user)) {
            return getUserFollowersResponse(user);
        }
        LOG.warning("User can not be null! : " + mapper.writeValueAsString(username));
        return createUserFollowersResponse(false, new HashSet<>(0));

    }

    @Override
    public UserFollowingsResponse getFollowings(String username) throws JsonProcessingException {
        User user = findByUsername(username);
        if (Objects.nonNull(user)) {
            return getUserFollowingsResponse(user);
        }
        LOG.warning("User can not be null! : " + mapper.writeValueAsString(username));
        return createUserFollowingsResponse(false, new HashSet<>(0));

    }

    @Override
    public GeneralResponse saveProfilePicture(UploadImageRequest uploadImageRequest) throws IOException {
        User user = userUtils.getUserFromSecurityContext();
        if (Objects.nonNull(user)) {
            if (Objects.nonNull(user.getProfileImage())) {
                imageService.deleteImage(user.getProfileImage());
            }
            user.setProfileImage(imageService.uploadImage(uploadImageRequest.getImage()));
            userRepository.save(user);
            return GeneralResponse.builder()
                    .isSuccess(true)
                    .message("Profile picture saved with success.")
                    .build();
        }
        return GeneralResponse.builder()
                .isSuccess(false)
                .message("Profile picture could not be saved.")
                .build();
    }

    @Transactional
    @Override
    public GeneralResponse removeProfilePicture() {
        User user = userUtils.getUserFromSecurityContext();

        if (Objects.isNull(user)) {
            return GeneralResponse.builder()
                    .isSuccess(false)
                    .message("User not found.")
                    .build();
        }

        try {
            removeProfileImage(user);
            return GeneralResponse.builder()
                    .isSuccess(true)
                    .message("Profile picture removed successfully.")
                    .build();
        } catch (Exception e) {
            LOG.warning("Error occurred while removing profile picture: " + e);

            return GeneralResponse.builder()
                    .isSuccess(false)
                    .message("Error occurred while removing profile picture.")
                    .build();
        }
    }

    private UserFollowersResponse getUserFollowersResponse(User user) {
        Set<FollowDataResponse> userFollowersDataResponses = user.getFollowers().stream().map(follower -> FollowDataResponse.builder()
                .username(follower.getUsername())
                .build()).collect(Collectors.toSet());

        return createUserFollowersResponse(true, userFollowersDataResponses);
    }

    private UserFollowingsResponse getUserFollowingsResponse(User user) {
        Set<FollowDataResponse> userFollowingsDataResponses = user.getFollowings().stream().map(following -> FollowDataResponse.builder()
                .username(following.getUsername())
                .build()).collect(Collectors.toSet());

        return createUserFollowingsResponse(true, userFollowingsDataResponses);
    }

    public UserFollowersResponse createUserFollowersResponse(Boolean isSuccess, Set<FollowDataResponse> followers) {
        return UserFollowersResponse.builder()
                .followers(followers)
                .isSuccess(isSuccess)
                .build();
    }
    public UserFollowingsResponse createUserFollowingsResponse(Boolean isSuccess, Set<FollowDataResponse> followings) {
        return UserFollowingsResponse.builder()
                .followings(followings)
                .isSuccess(isSuccess)
                .build();
    }

    private void removeProfileImage(User user) {
        Image profileImage = user.getProfileImage();

        if (Objects.nonNull(profileImage)) {
            user.setProfileImage(null);
            imageService.deleteImage(profileImage);
            userRepository.save(user);
        }

    }

}
