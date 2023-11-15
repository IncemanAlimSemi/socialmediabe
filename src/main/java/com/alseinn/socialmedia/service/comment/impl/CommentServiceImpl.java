package com.alseinn.socialmedia.service.comment.impl;

import com.alseinn.socialmedia.dao.user.CommentRepository;
import com.alseinn.socialmedia.entity.comment.Comment;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.comment.CreateCommentRequest;
import com.alseinn.socialmedia.request.comment.DeleteCommentRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.comment.CommentService;
import com.alseinn.socialmedia.service.post.PostService;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.ResponseUtils;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
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
    private final ResponseUtils responseUtils;

    private static final String LOCALIZATION = "tr";
    private static final String COMMENT = "Comment";
    private static final String POST = "Post";

    private static final Logger LOG = Logger.getLogger(CommentServiceImpl.class.getName());

    @Override
    public GeneralInformationResponse createComment(CreateCommentRequest createCommentRequest) throws IOException {
        User user = userService.findByUsername(createCommentRequest.getUsername());

        if (Objects.nonNull(user)) {
            if (!userUtils.isSessionUser(user)) {
                LOG.warning("This user is not session user -- Comment: " + mapper.writeValueAsString(createCommentRequest));
                return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(LOCALIZATION).getProperty("this.user.is.not.session.user"));
            }

            Post post = postService.findById(createCommentRequest.getId());

            if (Objects.isNull(post)) {
                LOG.warning("Post not found -- Comment: " + mapper.writeValueAsString(createCommentRequest));
                return responseUtils.createGeneralInformationResponse(false, MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("not.found"), POST));
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
                return responseUtils.createGeneralInformationResponse(true, MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("created.with.success"), COMMENT));
            } catch (Exception e) {
                LOG.warning("Comment could not be created -- Comment: " + mapper.writeValueAsString(post)
                        + " -- Username: " + user.getUsername());
                return responseUtils.createGeneralInformationResponse(false, MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("could.not.be.created"), COMMENT));
            }

        }

        LOG.warning("User not found -- Comment: " + mapper.writeValueAsString(createCommentRequest));
        return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(LOCALIZATION).getProperty("user.not.found"));
    }

    @Override
    public GeneralInformationResponse deleteComment(DeleteCommentRequest deleteCommentRequest) throws IOException {
        User user = userService.findByUsername(deleteCommentRequest.getUsername());

        if (Objects.nonNull(user)) {
            if (!userUtils.isSessionUser(user)) {
                LOG.warning("This user is not session user -- Comment: " + mapper.writeValueAsString(deleteCommentRequest));
                return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(LOCALIZATION).getProperty("this.user.is.not.session.user"));
            }

            Comment comment = findById(deleteCommentRequest.getId());


            if (Objects.nonNull(comment)) {
                if (!comment.getUser().getUsername().equals(user.getUsername()) &&
                    !comment.getPost().getUser().getUsername().equals(user.getUsername())
                ) {
                    LOG.warning("This user is not owner of post or comment -- Comment: " + mapper.writeValueAsString(comment)
                            + "-- Username: " + user.getUsername());
                    return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(LOCALIZATION).getProperty("this.user.is.not.owner.of.post.or.comment"));
                }

                try {
                    commentRepository.delete(comment);

                    LOG.info("Comment deleted with success -- Comment: " + mapper.writeValueAsString(comment)
                            + " -- Username: " + user.getUsername());
                    return responseUtils.createGeneralInformationResponse(true, MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("deleted.with.success"), COMMENT));
                } catch (Exception e) {
                    LOG.warning("Comment could not be deleted -- Comment: " + mapper.writeValueAsString(comment)
                            + " -- Username: " + user.getUsername());
                    return responseUtils.createGeneralInformationResponse(false, MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("could.not.be.deleted"), COMMENT));
                }
            }
            LOG.warning("Comment not found -- Comment: " + mapper.writeValueAsString(deleteCommentRequest)
                    + "-- Username: " + user.getUsername());
            return responseUtils.createGeneralInformationResponse(false, MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("not.found"), COMMENT));
        }

        LOG.warning("User not found -- Comment: " + mapper.writeValueAsString(deleteCommentRequest));
        return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(LOCALIZATION).getProperty("user.not.found"));
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

}
