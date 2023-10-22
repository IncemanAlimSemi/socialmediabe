package com.alseinn.socialmedia.controller.post;

import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.response.post.PostResponse;
import com.alseinn.socialmedia.service.post.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public PostResponse createPost(@RequestBody CreatePostRequest createPostRequest) throws JsonProcessingException {
        return postService.createPost(createPostRequest);
    }

    @DeleteMapping("/delete")
    public PostResponse deletePost(@RequestBody DeletePostRequest deletePostRequest) throws JsonProcessingException {
        return postService.deletePost(deletePostRequest);
    }
}
