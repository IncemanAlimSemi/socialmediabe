package com.alseinn.socialmedia.controller.like;

import com.alseinn.socialmedia.request.like.LikeActionRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.like.LikeActionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("${default.api.path}/like")
@RequiredArgsConstructor
public class LikeActionController {

    private final LikeActionService likeActionService;

    @PostMapping()
    public ResponseEntity<GeneralInformationResponse> follow(
            @Valid @RequestBody LikeActionRequest likeActionRequest,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ) throws IOException {
        return ResponseEntity.ok(likeActionService.like(likeActionRequest));
    }

}
