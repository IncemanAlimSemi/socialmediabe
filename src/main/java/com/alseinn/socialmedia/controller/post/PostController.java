package com.alseinn.socialmedia.controller.post;

import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<GeneralInformationResponse> createPost(@RequestBody CreatePostRequest createPostRequest) throws IOException {
        return ResponseEntity.ok(postService.createPost(createPostRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GeneralInformationResponse> deletePost(@RequestBody DeletePostRequest deletePostRequest) throws IOException {
        return ResponseEntity.ok(postService.deletePost(deletePostRequest));
    }
}
