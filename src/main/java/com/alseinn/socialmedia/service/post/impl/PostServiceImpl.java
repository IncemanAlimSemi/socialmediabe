package com.alseinn.socialmedia.service.post.impl;

import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.dao.user.PostRepository;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.response.post.PostResponse;
import com.alseinn.socialmedia.service.post.PostService;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final ObjectMapper mapper;
    private final UserUtils userUtils;

    private static final Logger LOG = Logger.getLogger(PostServiceImpl.class.getName());

    @Override
    public PostResponse createPost(CreatePostRequest createPostRequest) throws JsonProcessingException {
        User user = userService.findByUsername(createPostRequest.getUsername());
        if (Objects.nonNull(user)) {

            if (!isSessionUser(user)) {
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

                LOG.info("Post created with success -- Post: " + mapper.writeValueAsString(post)
                        + " -- Username: " + user.getUsername());

                return PostResponse.builder()
                        .message("")
                        .isSuccess(true)
                        .build();
            } catch (Exception e) {
                LOG.warning("Post could not be created -- Post: " + mapper.writeValueAsString(post)
                        + " -- Username: " + user.getUsername());
                return PostResponse.builder()
                        .message("Post could not be created")
                        .isSuccess(false)
                        .build();
            }

        }

        return userNotFoundResponse(createPostRequest);
    }

    @Override
    public PostResponse deletePost(DeletePostRequest deletePostRequest) throws JsonProcessingException {
        User user = userService.findByUsername(deletePostRequest.getUsername());

        if (Objects.nonNull(user)) {
            if (!isSessionUser(user)) {
                LOG.warning("This user is not session user -- Post: " + mapper.writeValueAsString(deletePostRequest));
                return PostResponse.builder()
                        .message("This user is not session user.")
                        .isSuccess(false)
                        .build();
            }
            Post post = postRepository.findById(deletePostRequest.getPostId()).orElse(null);
            if (Objects.nonNull(post)) {
                try {

                    postRepository.delete(post);

                    LOG.info("Post deleted with success -- Post: " + mapper.writeValueAsString(post)
                            + " -- Username: " + user.getUsername());
                    return PostResponse.builder()
                            .message("Post deleted")
                            .isSuccess(true)
                            .build();

                } catch (Exception e) {
                    LOG.warning("Post could not be deleted -- Post: " + mapper.writeValueAsString(post)
                            + " -- Username: " + user.getUsername());
                    return PostResponse.builder()
                            .message("Post could not be deleted")
                            .isSuccess(false)
                            .build();
                }
            }
            LOG.warning("Post not found -- Post: " + mapper.writeValueAsString(deletePostRequest)
                    + "-- Username: " + user.getUsername());
            return PostResponse.builder()
                    .message("Post not found")
                    .isSuccess(false)
                    .build();
        }

        return userNotFoundResponse(deletePostRequest);
    }

    private boolean isSessionUser(User user) {
        String sessionUsername = userUtils.getUserFromSecurityContext().getUsername();
        return Objects.equals(user.getUsername(), sessionUsername);
    }

    private <T> PostResponse userNotFoundResponse(T T) throws JsonProcessingException {
        LOG.warning("User nor found -- Post: " + mapper.writeValueAsString(T));
        return PostResponse.builder()
                .message("User not found")
                .isSuccess(false)
                .build();
    }
}
