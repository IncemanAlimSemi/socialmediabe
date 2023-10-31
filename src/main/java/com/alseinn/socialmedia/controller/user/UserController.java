package com.alseinn.socialmedia.controller.user;

import com.alseinn.socialmedia.response.user.UserFollowersResponse;
import com.alseinn.socialmedia.response.user.UserFollowingsResponse;
import com.alseinn.socialmedia.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/followers/{username}")
    public ResponseEntity<UserFollowersResponse> getFollowers(@PathVariable String username) throws JsonProcessingException {
        return ResponseEntity.ok(userService.getFollowers(username));
    }

    @GetMapping("/followings/{username}")
    public ResponseEntity<UserFollowingsResponse> getFollowings(@PathVariable String username) throws JsonProcessingException {
        return ResponseEntity.ok(userService.getFollowings(username));
    }


}