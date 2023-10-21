package com.alseinn.socialmedia.controller.post;

import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.response.post.PostResponse;
import com.alseinn.socialmedia.service.post.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public PostResponse createPost(@RequestBody CreatePostRequest createPostRequest) throws JsonProcessingException {
        return postService.createPost(createPostRequest);
    }
}
