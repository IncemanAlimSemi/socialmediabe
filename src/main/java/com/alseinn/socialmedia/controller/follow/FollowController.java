package com.alseinn.socialmedia.controller.follow;

import com.alseinn.socialmedia.request.follow.FollowRequest;
import com.alseinn.socialmedia.request.follow.UnfollowRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.follow.FollowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${default.api.path}/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/add")
    public ResponseEntity<GeneralInformationResponse> follow(
            @Valid @RequestBody FollowRequest followRequest,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ) throws JsonProcessingException {
        return ResponseEntity.ok(followService.follow(followRequest));
    }

    @PostMapping("/remove")
    public ResponseEntity<GeneralInformationResponse> unfollow(
            @Valid @RequestBody UnfollowRequest unfollowRequest,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ) throws JsonProcessingException {
        return ResponseEntity.ok(followService.unfollow(unfollowRequest));
    }


}
