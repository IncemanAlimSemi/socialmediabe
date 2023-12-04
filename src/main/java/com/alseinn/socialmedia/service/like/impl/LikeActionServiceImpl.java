package com.alseinn.socialmedia.service.like.impl;

import com.alseinn.socialmedia.dao.user.LikeActionRepository;
import com.alseinn.socialmedia.entity.item.Item;
import com.alseinn.socialmedia.entity.like.LikeAction;
import com.alseinn.socialmedia.entity.like.enums.ActionObjectEnum;
import com.alseinn.socialmedia.entity.user.User;
import com.alseinn.socialmedia.request.like.LikeActionRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.like.LikeActionService;
import com.alseinn.socialmedia.service.post.PostService;
import com.alseinn.socialmedia.service.user.UserService;
import com.alseinn.socialmedia.utils.ResponseUtils;
import com.alseinn.socialmedia.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

import static com.alseinn.socialmedia.utils.contants.AppTRConstants.ACTION;

@Service
@RequiredArgsConstructor
public class LikeActionServiceImpl implements LikeActionService {

    private final UserService userService;
    private final UserUtils userUtils;
    private final ObjectMapper mapper;
    private final PostService postService;
    private final LikeActionRepository likeActionRepository;
    private final ResponseUtils responseUtils;
    private final EntityManager entityManager;

    private static final Logger LOG = Logger.getLogger(LikeActionServiceImpl.class.getName());

    @Override
    public GeneralInformationResponse like(LikeActionRequest likeActionRequest) throws IOException {
        User user = userService.findByUsername(likeActionRequest.getUsername());

        if (Objects.nonNull(user)) {
            if (!userUtils.isSessionUser(user)) {
                LOG.warning(responseUtils.getMessage("this.user.is.not.session.user") + " -- Action: " + mapper.writeValueAsString(likeActionRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("this.user.is.not.session.user"));
            }

            if (!isActionObjectFoundInDatabase(likeActionRequest.getType(), likeActionRequest.getId())) {
                LOG.warning(responseUtils.getMessage("this.id.is.not.found.in.database", ACTION, likeActionRequest.getId()) + " -- Action: " + mapper.writeValueAsString(likeActionRequest));
                return responseUtils.createGeneralInformationResponse(false, responseUtils.getMessage("this.id.is.not.found.in.database", ACTION, likeActionRequest.getId()));
            }

            LikeAction likeAction = findByUsernameAndActionObjectAndActionObjectId(likeActionRequest.getUsername(), likeActionRequest.getType(), likeActionRequest.getId());

            try {
                if (Objects.isNull(likeAction)) {
                    LikeAction newLikeAction = LikeAction.builder()
                            .username(likeActionRequest.getUsername())
                            .actionObject(likeActionRequest.getType())
                            .actionObjectId(likeActionRequest.getId())
                            .timeCreated(new Date(System.currentTimeMillis()))
                            .timeModified(new Date(System.currentTimeMillis()))
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
    public LikeAction findById(Long id) {
        return likeActionRepository.findById(id).orElse(null);
    }

    @Override
    public LikeAction findByUsernameAndActionObjectAndActionObjectId(String username, ActionObjectEnum type, Long id) {
        return likeActionRepository.findByUsernameAndActionObjectAndActionObjectId(username, type, id).orElse(null);
    }

    private Boolean isActionObjectFoundInDatabase(ActionObjectEnum type, Long id) {
        Item item = entityManager.find(Item.class, id);
        return Objects.nonNull(item) && type.toString().equalsIgnoreCase(item.getClass().getAnnotation(Table.class).name());
    }

}
