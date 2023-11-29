package com.alseinn.socialmedia.controller.post;

import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${default.api.path}/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<GeneralInformationResponse> createPost(
            @Valid @RequestBody CreatePostRequest createPostRequest,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ) throws IOException {
        return ResponseEntity.ok(postService.createPost(createPostRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GeneralInformationResponse> deletePost(
            @Valid @RequestBody DeletePostRequest deletePostRequest,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ) throws IOException {
        return ResponseEntity.ok(postService.deletePost(deletePostRequest));
    }

    @GetMapping("/{username}")
    public ResponseEntity<GeneralInformationResponse> findByUserOrderByDate(
            @PathVariable String username) throws IOException {
        return ResponseEntity.ok(postService.findByUserOrderByDate(username));
    }
}
