package com.alseinn.socialmedia.controller.user;

import com.alseinn.socialmedia.request.image.UploadImageRequest;
import com.alseinn.socialmedia.response.follow.UserFollowersResponse;
import com.alseinn.socialmedia.response.follow.UserFollowingsResponse;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${default.api.path}/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile/{username}")
    public ResponseEntity<GeneralInformationResponse> getProfile(
            @PathVariable String username
    ) throws IOException {
        return ResponseEntity.ok(userService.getProfile(username));
    }

    @GetMapping("/profile/other/{username}")
    public ResponseEntity<GeneralInformationResponse> getOtherProfile(
            @Valid @PathVariable String username
    ) throws IOException {
        return ResponseEntity.ok(userService.getOtherProfile(username));
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<UserFollowersResponse> getFollowers(
            @PathVariable String username
    ) throws JsonProcessingException {
        return ResponseEntity.ok(userService.getFollowers(username));
    }

    @GetMapping("/followings/{username}")
    public ResponseEntity<UserFollowingsResponse> getFollowings(
            @PathVariable String username
    ) throws JsonProcessingException {
        return ResponseEntity.ok(userService.getFollowings(username));
    }


    @GetMapping("/picture/upload")
    public ResponseEntity<GeneralInformationResponse> saveProfilePicture(
            @ModelAttribute UploadImageRequest uploadImageRequest,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ) throws IOException {
        return ResponseEntity.ok(userService.updateProfilePicture(uploadImageRequest));
    }

    @GetMapping("/picture/remove")
    public ResponseEntity<GeneralInformationResponse> removeProfilePicture() throws IOException {
        return ResponseEntity.ok(userService.removeProfilePicture());
    }

}