package com.alseinn.socialmedia.service.like;

import com.alseinn.socialmedia.entity.like.LikeAction;
import com.alseinn.socialmedia.entity.like.LikeActionKey;
import com.alseinn.socialmedia.request.like.LikeActionRequest;
import com.alseinn.socialmedia.response.like.ActionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface LikeActionService {

    ActionResponse like(LikeActionRequest likeActionRequest) throws JsonProcessingException;

    LikeAction findById(LikeActionKey id);
}
