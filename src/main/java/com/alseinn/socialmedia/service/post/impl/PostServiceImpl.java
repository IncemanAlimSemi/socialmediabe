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
                        .isSuccess(false)
                        .message("This user is not session user.")
                        .build();
            }

            Post post = Post.builder()
                    .title(createPostRequest.getTitle())
                    .content(createPostRequest.getContent())
                    .postLike(0)
                    .postUnlike(0)
                    .user(user)
                    .build();

            try {
                postRepository.save(post);

                LOG.info("Post created with success -- Post: " + mapper.writeValueAsString(post)
                        + " -- Username: " + user.getUsername());

                return PostResponse.builder()
                        .isSuccess(true)
                        .message("Post created with success.")
                        .build();
            } catch (Exception e) {
                LOG.warning("Post could not be created -- Post: " + mapper.writeValueAsString(post)
                        + " -- Username: " + user.getUsername());
                return PostResponse.builder()
                        .isSuccess(false)
                        .message("Post could not be created.")
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
                        .isSuccess(false)
                        .message("This user is not session user.")
                        .build();
            }
            Post post = postRepository.findById(deletePostRequest.getId()).orElse(null);
            if (Objects.nonNull(post)) {
                if (!post.getUser().getUsername().equals(user.getUsername())) {
                    LOG.warning("This user is not owner this post -- Post: " + mapper.writeValueAsString(post));
                    return PostResponse.builder()
                            .isSuccess(false)
                            .message("This user is not owner this post.")
                            .build();
                }

                try {

                    postRepository.delete(post);

                    LOG.info("Post deleted with success -- Post: " + mapper.writeValueAsString(post)
                            + " -- Username: " + user.getUsername());
                    return PostResponse.builder()
                            .isSuccess(true)
                            .message("Post deleted.")
                            .build();

                } catch (Exception e) {
                    LOG.warning("Post could not be deleted -- Post: " + mapper.writeValueAsString(post)
                            + " -- Username: " + user.getUsername());
                    return PostResponse.builder()
                            .isSuccess(false)
                            .message("Post could not be deleted.")
                            .build();
                }
            }
            LOG.warning("Post not found -- Post: " + mapper.writeValueAsString(deletePostRequest)
                    + "-- Username: " + user.getUsername());
            return PostResponse.builder()
                    .isSuccess(false)
                    .message("Post not found.")
                    .build();
        }

        return userNotFoundResponse(deletePostRequest);
    }

    private boolean isSessionUser(User user) {
        String sessionUsername = userUtils.getUserFromSecurityContext().getUsername();
        return Objects.equals(user.getUsername(), sessionUsername);
    }

    private <T> PostResponse userNotFoundResponse(T T) throws JsonProcessingException {
        LOG.warning("User not found -- Post: " + mapper.writeValueAsString(T));
        return PostResponse.builder()
                .isSuccess(false)
                .message("User not found.")
                .build();
    }
}
