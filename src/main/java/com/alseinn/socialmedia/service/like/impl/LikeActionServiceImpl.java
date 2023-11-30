package com.alseinn.socialmedia.service.like.impl;

import com.alseinn.socialmedia.dao.user.LikeActionRepository;
import com.alseinn.socialmedia.entity.like.LikeAction;
import com.alseinn.socialmedia.entity.like.LikeActionKey;
import com.alseinn.socialmedia.entity.like.enums.ActionObjectEnum;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.like.LikeActionRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.comment.CommentService;
import com.alseinn.socialmedia.service.like.LikeActionService;
import com.alseinn.socialmedia.service.post.PostService;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.ResponseUtils;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

import static com.alseinn.socialmedia.utils.contants.AppTRConstants.ACTION;

@Service
@RequiredArgsConstructor
public class LikeActionServiceImpl implements LikeActionService {

    private final UserService userService;
    private final UserUtils userUtils;
    private final ObjectMapper mapper;
    private final PostService postService;
    private final CommentService commentService;
    private final LikeActionRepository likeActionRepository;
    private final ResponseUtils responseUtils;

    private static final Logger LOG = Logger.getLogger(LikeActionServiceImpl.class.getName());

    @Override
    public GeneralInformationResponse like(LikeActionRequest likeActionRequest) throws IOException {
        User user = userService.findByUsername(likeActionRequest.getUsername());

        if (Objects.nonNull(user)) {
            if (!userUtils.isSessionUser(user)) {
                LOG.warning(responseUtils.getMessage("this.user.is.not.session.user") + " -- Action: " + mapper.writeValueAsString(likeActionRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("this.user.is.not.session.user"));
            }

            if (isActionObjectNotFoundInDatabase(likeActionRequest.getType(), likeActionRequest.getId())) {
                LOG.warning(responseUtils.getMessage("this.id.is.not.found.in.database", ACTION, likeActionRequest.getId()) + " -- Action: " + mapper.writeValueAsString(likeActionRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("this.id.is.not.found.in.database", ACTION, likeActionRequest.getId()));
            }

            LikeActionKey likeActionKey = LikeActionKey.builder()
                    .actionObject(likeActionRequest.getType())
                    .actionObjectId(likeActionRequest.getId())
                    .username(likeActionRequest.getUsername())
                    .build();

            LikeAction likeAction = findById(likeActionKey);

            try {
                if (Objects.isNull(likeAction)) {
                    LikeAction newLikeAction = LikeAction.builder()
                            .id(likeActionKey)
                            .build();

                    likeActionRepository.save(newLikeAction);

                    if (ActionObjectEnum.POST.equals(likeActionRequest.getType())) {
                        postService.setPostLikeOrUnlike(true, likeActionRequest.getId());
                    }

                    LOG.info("This " + likeActionRequest.getType().toString() + " liked with success -- Action: "
                            + mapper.writeValueAsString(likeActionRequest));
                    return responseUtils.createGeneralInformationResponse(true, responseUtils.getMessage("liked", likeActionRequest.getType().toString()));
                } else {
                    likeActionRepository.delete(likeAction);

                    if (ActionObjectEnum.POST.equals(likeActionRequest.getType())) {
                        postService.setPostLikeOrUnlike(false, likeActionRequest.getId());
                    }

                    LOG.info("This " + likeActionRequest.getType().toString() + " unliked with success -- Action: "
                            + mapper.writeValueAsString(likeActionRequest));
                    return responseUtils.createGeneralInformationResponse(true, responseUtils.getMessage("unliked", likeActionRequest.getType().toString()));
                }
            } catch (Exception e) {
                LOG.warning("This " + likeActionRequest.getType().toString() + " not liked/unliked -- Action: "
                        + mapper.writeValueAsString(likeActionRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("liked.unliked" , likeActionRequest.getType().toString()));
            }

        }

        LOG.warning(responseUtils.getMessage("user.not.found") + " -- Action: " + mapper.writeValueAsString(likeActionRequest));
        return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("user.not.found"));
    }

    @Override
    public LikeAction findById(LikeActionKey id) {
        return likeActionRepository.findById(id).orElse(null);
    }

    private Boolean isActionObjectNotFoundInDatabase(ActionObjectEnum type, Long id) {
        final HashMap<ActionObjectEnum, Function<Long, Boolean>> map = new HashMap<>() {{
            put(ActionObjectEnum.POST, id -> Objects.isNull(postService.findById(id)));
            put(ActionObjectEnum.COMMENT, id -> Objects.isNull(commentService.findById(id)));
        }};

        return map.get(type).apply(id);
    }

}
