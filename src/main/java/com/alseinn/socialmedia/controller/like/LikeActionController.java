package com.alseinn.socialmedia.controller.like;

import com.alseinn.socialmedia.request.like.LikeActionRequest;
import com.alseinn.socialmedia.response.like.ActionResponse;
import com.alseinn.socialmedia.service.like.LikeActionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeActionController {

    private final LikeActionService likeActionService;

    @PostMapping()
    public ResponseEntity<ActionResponse> follow(@RequestBody LikeActionRequest likeActionRequest) throws JsonProcessingException {
        return ResponseEntity.ok(likeActionService.like(likeActionRequest));
    }

}
