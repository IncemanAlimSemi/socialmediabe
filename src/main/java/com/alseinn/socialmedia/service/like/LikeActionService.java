package com.alseinn.socialmedia.service.like;

import com.alseinn.socialmedia.entity.like.LikeAction;
import com.alseinn.socialmedia.entity.like.LikeActionKey;
import com.alseinn.socialmedia.request.like.LikeActionRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;

import java.io.IOException;

public interface LikeActionService {

    GeneralInformationResponse like(LikeActionRequest likeActionRequest) throws IOException;

    LikeAction findById(LikeActionKey id);
}
