package com.alseinn.socialmedia.service.post.impl;

import com.alseinn.socialmedia.request.post.DeletePostRequest;
import com.alseinn.socialmedia.dao.user.PostRepository;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.post.CreatePostRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
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
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final ObjectMapper mapper;
    private final UserUtils userUtils;
    private final ResponseUtils responseUtils;

    private static final String localization = "tr";
    private static final String POST = "Post";

    private static final Logger LOG = Logger.getLogger(PostServiceImpl.class.getName());

    @Override
    public GeneralInformationResponse createPost(CreatePostRequest createPostRequest) throws IOException {
        User user = userService.findByUsername(createPostRequest.getUsername());

        if (Objects.nonNull(user)) {

            if (!userUtils.isSessionUser(user)) {
                LOG.warning("This user is not session user -- Post: " + mapper.writeValueAsString(createPostRequest));
                return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(localization).getProperty("this.user.is.not.session.user"));
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

                return responseUtils.createGeneralInformationResponse(true, MessageFormat.format(ResponseUtils.getProperties(localization).getProperty("created.with.success"), POST));
            } catch (Exception e) {
                LOG.warning("Post could not be created -- Post: " + mapper.writeValueAsString(post)
                        + " -- Username: " + user.getUsername());
                return responseUtils.createGeneralInformationResponse(false, MessageFormat.format(ResponseUtils.getProperties(localization).getProperty("could.not.be.created"), POST));
            }

        }

        LOG.warning("User not found -- Post: " + mapper.writeValueAsString(createPostRequest));
        return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(localization).getProperty("user.not.found"));
    }

    @Override
    public GeneralInformationResponse deletePost(DeletePostRequest deletePostRequest) throws IOException {
        User user = userService.findByUsername(deletePostRequest.getUsername());

        if (Objects.nonNull(user)) {
            if (!userUtils.isSessionUser(user)) {
                LOG.warning("This user is not session user -- Post: " + mapper.writeValueAsString(deletePostRequest));
                return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(localization).getProperty("this.user.is.not.session.user"));
            }
            Post post = postRepository.findById(deletePostRequest.getId()).orElse(null);
            if (Objects.nonNull(post)) {
                if (!post.getUser().getUsername().equals(user.getUsername())) {
                    LOG.warning("This user is not owner this post -- Post: " + mapper.writeValueAsString(post));
                    return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(localization).getProperty("this.user.is.not.owner.this.post"));
                }

                try {
                    postRepository.delete(post);

                    LOG.info("Post deleted with success -- Post: " + mapper.writeValueAsString(post)
                            + " -- Username: " + user.getUsername());

                    return responseUtils.createGeneralInformationResponse(true, MessageFormat.format(ResponseUtils.getProperties(localization).getProperty("deleted.with.success"), POST));
                } catch (Exception e) {
                    LOG.warning("Post could not be deleted -- Post: " + mapper.writeValueAsString(post)
                            + " -- Username: " + user.getUsername());
                    return responseUtils.createGeneralInformationResponse(false, MessageFormat.format(ResponseUtils.getProperties(localization).getProperty("could.not.be.deleted"), POST));
                }
            }
            LOG.warning("Post not found -- Post: " + mapper.writeValueAsString(deletePostRequest)
                    + "-- Username: " + user.getUsername());
            return responseUtils.createGeneralInformationResponse(false, MessageFormat.format(ResponseUtils.getProperties(localization).getProperty("not.found"), POST));
        }

        LOG.warning("User not found -- Post: " + mapper.writeValueAsString(deletePostRequest));
        return responseUtils.createGeneralInformationResponse(false, ResponseUtils.getProperties(localization).getProperty("user.not.found"));
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}
