package com.alseinn.socialmedia.service.comment.impl;

import com.alseinn.socialmedia.dao.user.CommentRepository;
import com.alseinn.socialmedia.entity.comment.Comment;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.comment.CreateCommentRequest;
import com.alseinn.socialmedia.request.comment.DeleteCommentRequest;
import com.alseinn.socialmedia.response.comment.CommentResponse;
import com.alseinn.socialmedia.service.comment.CommentService;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final UserUtils userUtils;
    private final ObjectMapper mapper;
    private final PostService postService;

    private static final Logger LOG = Logger.getLogger(CommentServiceImpl.class.getName());

    @Override
    public CommentResponse createComment(CreateCommentRequest createCommentRequest) throws JsonProcessingException {
        User user = userService.findByUsername(createCommentRequest.getUsername());

        if (Objects.nonNull(user)) {
            if (!userUtils.isSessionUser(user)) {
                LOG.warning("This user is not session user -- Comment: " + mapper.writeValueAsString(createCommentRequest));
                return CommentResponse.builder()
                        .isSuccess(false)
                        .message("This user is not session user.")
                        .build();
            }

            Post post = postService.findById(createCommentRequest.getId());

            if (Objects.isNull(post)) {
                LOG.warning("Post not found -- Comment: " + mapper.writeValueAsString(createCommentRequest));
                return CommentResponse.builder()
                        .isSuccess(false)
                        .message("Post not found.")
                        .build();
            }

            Comment comment = Comment.builder()
                    .content(createCommentRequest.getContent())
                    .post(post)
                    .user(user)
                    .build();

            try {
                commentRepository.save(comment);
                LOG.info("Comment created with success -- Comment: " + mapper.writeValueAsString(comment)
                        + " -- Username: " + user.getUsername());

                return CommentResponse.builder()
                        .isSuccess(true)
                        .message("Comment created with success.")
                        .build();
            } catch (Exception e) {
                LOG.warning("Comment could not be created -- Comment: " + mapper.writeValueAsString(post)
                        + " -- Username: " + user.getUsername());
                return CommentResponse.builder()
                        .isSuccess(false)
                        .message("Comment could not be created.")
                        .build();
            }

        }

        return userNotFoundResponse(createCommentRequest);
    }

    @Override
    public CommentResponse deleteComment(DeleteCommentRequest deleteCommentRequest) throws JsonProcessingException {
        User user = userService.findByUsername(deleteCommentRequest.getUsername());

        if (Objects.nonNull(user)) {
            if (!userUtils.isSessionUser(user)) {
                LOG.warning("This user is not session user -- Comment: " + mapper.writeValueAsString(deleteCommentRequest));
                return CommentResponse.builder()
                        .isSuccess(false)
                        .message("This user is not session user.")
                        .build();
            }

            Comment comment = commentRepository.findById(deleteCommentRequest.getId()).orElse(null);


            if (Objects.nonNull(comment)) {
                if (!comment.getUser().getUsername().equals(user.getUsername()) &&
                    !comment.getPost().getUser().getUsername().equals(user.getUsername())
                ) {
                    LOG.warning("This user is not owner of post or comment -- Comment: " + mapper.writeValueAsString(comment)
                            + "-- Username: " + user.getUsername());
                    return CommentResponse.builder()
                            .isSuccess(false)
                            .message("This user is not owner of post or comment.")
                            .build();
                }

                try {
                    commentRepository.delete(comment);

                    LOG.info("Comment deleted with success -- Comment: " + mapper.writeValueAsString(comment)
                            + " -- Username: " + user.getUsername());

                    return CommentResponse.builder()
                            .isSuccess(true)
                            .message("Comment deleted with success.")
                            .build();
                } catch (Exception e) {
                    LOG.warning("Comment could not be deleted -- Comment: " + mapper.writeValueAsString(comment)
                            + " -- Username: " + user.getUsername());
                    return CommentResponse.builder()
                            .isSuccess(false)
                            .message("Comment could not be deleted.")
                            .build();
                }
            }
            LOG.warning("Comment not found -- Comment: " + mapper.writeValueAsString(deleteCommentRequest)
                    + "-- Username: " + user.getUsername());
            return CommentResponse.builder()
                    .isSuccess(false)
                    .message("Comment not found.")
                    .build();
        }

        return userNotFoundResponse(deleteCommentRequest);
    }

    private <T> CommentResponse userNotFoundResponse(T T) throws JsonProcessingException {
        LOG.warning("User not found -- Comment: " + mapper.writeValueAsString(T));
        return CommentResponse.builder()
                .isSuccess(false)
                .message("User not found.")
                .build();
    }
}
