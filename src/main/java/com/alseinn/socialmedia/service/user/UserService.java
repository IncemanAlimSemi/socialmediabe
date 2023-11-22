package com.alseinn.socialmedia.service.user;

import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.image.UploadImageRequest;
import com.alseinn.socialmedia.response.follow.UserFollowersResponse;
import com.alseinn.socialmedia.response.follow.UserFollowingsResponse;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface UserService {
    User findByUsername(String username);

    GeneralInformationResponse getProfile(String username) throws IOException;

    UserFollowersResponse getFollowers(String username) throws JsonProcessingException;

    UserFollowingsResponse getFollowings(String username) throws JsonProcessingException;

    GeneralInformationResponse updateProfilePicture(UploadImageRequest uploadImageRequest) throws IOException;

    GeneralInformationResponse removeProfilePicture() throws IOException;

}
