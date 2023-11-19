package com.alseinn.socialmedia.service.post;

import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;

import java.io.IOException;
import java.util.List;

public interface PostService {

    GeneralInformationResponse createPost(CreatePostRequest createPostRequest) throws IOException;
    GeneralInformationResponse deletePost(DeletePostRequest deletePostRequest) throws IOException;
    Post findById(Long id);
    GeneralInformationResponse findByUserOrderByDate(String username) throws IOException;

}
