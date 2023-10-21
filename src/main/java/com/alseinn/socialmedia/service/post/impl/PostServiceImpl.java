package com.alseinn.socialmedia.service.post.impl;

import com.alseinn.socialmedia.dao.user.PostRepository;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.response.post.PostResponse;
import com.alseinn.socialmedia.service.post.PostService;
import com.alseinn.socialmedia.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final ObjectMapper mapper;

    private static final Logger LOG = Logger.getLogger(PostServiceImpl.class.getName());

    @Override
    public PostResponse createPost(CreatePostRequest createPostRequest) throws JsonProcessingException {
        User user = userService.findByUsername(createPostRequest.getUsername());
        if (Objects.nonNull(user)) {

            String sessionUsername = ((User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal()).getUsername();

            if (!Objects.equals(sessionUsername, user.getUsername())) {
                LOG.warning("This user is not session user -- Post: " + mapper.writeValueAsString(createPostRequest));
                return PostResponse.builder()
                        .message("This user is not session user.")
                        .isSuccess(false)
                        .build();
            }

            Post post = Post.builder()
                    .postTitle(createPostRequest.getPostTitle())
                    .postContent(createPostRequest.getPostContent())
                    .postLike(0)
                    .postUnlike(0)
                    .user(user)
                    .build();

            try {
                postRepository.save(post);

                LOG.info("Post Created With Success -- Post: " + mapper.writeValueAsString(post) + " -- Username: " + sessionUsername);

                return PostResponse.builder()
                        .message("")
                        .isSuccess(true)
                        .build();
            } catch (Exception e) {
                LOG.warning("Post could not be created -- Post: " + mapper.writeValueAsString(post) + " -- Username: " + sessionUsername);
                return PostResponse.builder()
                        .message("Post could not be created")
                        .isSuccess(false)
                        .build();
            }

        }
        LOG.warning("User nor found -- Post: " + mapper.writeValueAsString(createPostRequest));
        return PostResponse.builder()
                .message("User not found")
                .isSuccess(false)
                .build();
    }
}
