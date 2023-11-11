package com.alseinn.socialmedia.service.user;

import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.image.UploadImageRequest;
import com.alseinn.socialmedia.response.GeneralResponse;
import com.alseinn.socialmedia.response.follow.UserFollowersResponse;
import com.alseinn.socialmedia.response.follow.UserFollowingsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface UserService {
    User findByUsername(String username);

    GeneralResponse getUserDetail(String username) throws IOException;

    UserFollowersResponse getFollowers(String username) throws JsonProcessingException;

    UserFollowingsResponse getFollowings(String username) throws JsonProcessingException;

    GeneralResponse saveProfilePicture(UploadImageRequest uploadImageRequest) throws IOException;

    GeneralResponse removeProfilePicture();

}
