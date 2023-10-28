package com.alseinn.socialmedia.controller.follow;

import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.request.follow.UnfollowRequest;
import com.alseinn.socialmedia.response.follow.FollowResponse;
import com.alseinn.socialmedia.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/add")
    public ResponseEntity<FollowResponse> follow(@RequestBody FollowRequest followRequest) {
        return ResponseEntity.ok(followService.follow(followRequest));
    }

    @PostMapping("/remove")
    public ResponseEntity<FollowResponse> unfollow(@RequestBody UnfollowRequest unfollowRequest) {
        return ResponseEntity.ok(followService.unfollow(unfollowRequest));
    }


}
