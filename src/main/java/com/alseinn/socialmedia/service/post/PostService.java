package com.alseinn.socialmedia.service.post;

import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.response.post.PostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PostService {

    PostResponse createPost(CreatePostRequest createPostRequest) throws JsonProcessingException;
    PostResponse deletePost(DeletePostRequest deletePostRequest) throws JsonProcessingException;

}
