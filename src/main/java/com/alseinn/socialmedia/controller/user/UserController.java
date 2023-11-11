package com.alseinn.socialmedia.controller.user;

import com.alseinn.socialmedia.request.image.UploadImageRequest;
import com.alseinn.socialmedia.response.GeneralResponse;
import com.alseinn.socialmedia.response.follow.UserFollowersResponse;
import com.alseinn.socialmedia.response.follow.UserFollowingsResponse;
import com.alseinn.socialmedia.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile/{username}")
    public ResponseEntity<GeneralResponse> getProfile(@PathVariable String username) throws IOException {
        return ResponseEntity.ok(userService.getUserDetail(username));
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<UserFollowersResponse> getFollowers(@PathVariable String username) throws JsonProcessingException {
        return ResponseEntity.ok(userService.getFollowers(username));
    }

    @GetMapping("/followings/{username}")
    public ResponseEntity<UserFollowingsResponse> getFollowings(@PathVariable String username) throws JsonProcessingException {
        return ResponseEntity.ok(userService.getFollowings(username));
    }


    @GetMapping("/picture/upload")
    public ResponseEntity<GeneralResponse> saveProfilePicture(@ModelAttribute UploadImageRequest uploadImageRequest) throws IOException {
        return ResponseEntity.ok(userService.saveProfilePicture(uploadImageRequest));
    }

    @GetMapping("/picture/remove")
    public ResponseEntity<GeneralResponse> removeProfilePicture() {
        return ResponseEntity.ok(userService.removeProfilePicture());
    }

}