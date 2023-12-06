package com.alseinn.socialmedia.service.post.impl;

import com.alseinn.socialmedia.entity.comment.Comment;
import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.dao.user.PostRepository;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.response.comment.CommentDetailResponse;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.response.post.PostDetailResponse;
import com.alseinn.socialmedia.response.user.UsersPostResponse;
import com.alseinn.socialmedia.service.post.PostService;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.ResponseUtils;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.io.IOException;
import java.util.logging.Logger;

import static com.alseinn.socialmedia.utils.contants.AppTRConstants.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final ObjectMapper mapper;
    private final UserUtils userUtils;
    private final ResponseUtils responseUtils;

    private static final Logger LOG = Logger.getLogger(PostServiceImpl.class.getName());

    @Override
    public GeneralInformationResponse createPost(CreatePostRequest createPostRequest) throws IOException {
        User user = userService.findByUsername(createPostRequest.getUsername());

        if (Objects.nonNull(user)) {

            if (!userUtils.isSessionUser(user)) {
                LOG.warning(responseUtils.getMessage("this.user.is.not.session.user") + " -- Post: " + mapper.writeValueAsString(createPostRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("this.user.is.not.session.user"));
            }

            Post post = Post.builder()
                    .title(createPostRequest.getTitle())
                    .content(createPostRequest.getContent())
                    .postLike(0)
                    .user(user)
                    .timeCreated(new Date(System.currentTimeMillis()))
                    .timeModified(new Date(System.currentTimeMillis()))
                    .build();

            try {
                postRepository.save(post);

                LOG.info(responseUtils.getMessage("created.with.success", POST) + " -- Post: " + mapper.writeValueAsString(post)
                        + " -- Username: " + user.getUsername());

                return responseUtils.createGeneralInformationResponse(true, responseUtils.getMessage("created.with.success", POST));
            } catch (Exception e) {
                LOG.warning(responseUtils.getMessage("could.not.be.created", POST) + " -- Post: " + mapper.writeValueAsString(post)
                        + " -- Username: " + user.getUsername());
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("could.not.be.created", POST));
            }

        }

        LOG.warning(responseUtils.getMessage("user.not.found") + " -- Post: " + mapper.writeValueAsString(createPostRequest));
        return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("user.not.found"));
    }

    @Override
    public GeneralInformationResponse deletePost(DeletePostRequest deletePostRequest) throws IOException {
        User user = userService.findByUsername(deletePostRequest.getUsername());

        if (Objects.nonNull(user)) {
            if (!userUtils.isSessionUser(user)) {
                LOG.warning(responseUtils.getMessage("this.user.is.not.session.user") + " -- Post: " + mapper.writeValueAsString(deletePostRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("this.user.is.not.session.user"));
            }
            Post post = postRepository.findById(deletePostRequest.getId()).orElse(null);
            if (Objects.nonNull(post)) {
                if (!post.getUser().getUsername().equals(user.getUsername())) {
                    LOG.warning(responseUtils.getMessage("this.user.is.not.owner.this.post") + " -- Post: " + mapper.writeValueAsString(post));
                    return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("this.user.is.not.owner.this.post"));
                }

                try {
                    postRepository.delete(post);

                    LOG.info(responseUtils.getMessage("deleted.with.success", POST) + " -- Post: " + mapper.writeValueAsString(post)
                            + " -- Username: " + user.getUsername());

                    return responseUtils.createGeneralInformationResponse(true, responseUtils.getMessage("deleted.with.success", POST));
                } catch (Exception e) {
                    LOG.warning(responseUtils.getMessage("could.not.be.deleted", POST) + " -- Post: " + mapper.writeValueAsString(post)
                            + " -- Username: " + user.getUsername());
                    return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("could.not.be.deleted", POST));
                }
            }
            LOG.warning(responseUtils.getMessage("not.found", POST) + " -- Post: " + mapper.writeValueAsString(deletePostRequest)
                    + "-- Username: " + user.getUsername());
            return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("not.found", POST));
        }

        LOG.warning(responseUtils.getMessage("user.not.found") + " -- Post: " + mapper.writeValueAsString(deletePostRequest));
        return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("user.not.found"));
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public GeneralInformationResponse findByUserOrderByDate(String username) throws IOException {
        User user = userService.findByUsername(username);

        if (Objects.nonNull(user)) {
            if (!userUtils.isSessionUser(user)) {
                LOG.warning(responseUtils.getMessage("this.user.is.not.session.user") + " -- Username: " + mapper.writeValueAsString(username));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("this.user.is.not.session.user"));
            }

            List<Post> posts = postRepository.findByUserOrderByDate(user);

            if (CollectionUtils.isEmpty(posts)) {
                LOG.warning(responseUtils.getMessage("no.posts.found.by.the.user.in.x.hours", HOURS_24) + " -- Username: " + mapper.writeValueAsString(username));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("no.posts.found.by.the.user.in.x.hours", HOURS_24));
            }

            try {
                LOG.info(responseUtils.getMessage("posts.are.get.with.success") + " -- Username: " + mapper.writeValueAsString(username));
                return UsersPostResponse.builder()
                        .username(username)
                        .postDetailResponses(createPostDetailResponses(posts))
                        .isSuccess(true)
                        .message(responseUtils.getMessage("posts.are.get.with.success"))
                        .build();

            } catch (Exception e) {
                LOG.info(responseUtils.getMessage("posts.could.not.get.with.success") + " -- Username: " + mapper.writeValueAsString(username));
                return responseUtils.createGeneralInformationResponse(false,
                        responseUtils.getMessage("posts.could.not.get.with.success"));
            }
        }

        LOG.warning(responseUtils.getMessage("user.not.found") + " -- Username: " + mapper.writeValueAsString(username));
        return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("user.not.found"));
    }

    private List<PostDetailResponse> createPostDetailResponses(List<Post> posts) {
        List<PostDetailResponse> postDetailResponses = new ArrayList<>();
        posts.forEach(post -> postDetailResponses.add(PostDetailResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .like(post.getPostLike())
                .date(post.getTimeCreated())
                .commentDetailResponses(createCommentDetailResponses(post.getComments()))
                .build())
        );

        return postDetailResponses;
    }

    private List<CommentDetailResponse> createCommentDetailResponses(List<Comment> comments) {
        List<CommentDetailResponse> commentDetailResponses = new ArrayList<>();
        comments.forEach(comment -> commentDetailResponses.add(CommentDetailResponse.builder()
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .date(comment.getDate())
                .build())
        );

        return commentDetailResponses;
    }

    public void setPostLikeOrUnlike(Boolean isLike, Long id) {
        Post post = findById(id);
        long like = post.getPostLike();
        if (isLike) {
            post.setPostLike(like + 1);
        } else if (like > 0){
            post.setPostLike(post.getPostLike() - 1);
        }
        postRepository.save(post);
    }
}
