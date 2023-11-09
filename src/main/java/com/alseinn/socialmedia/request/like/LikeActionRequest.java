package com.alseinn.socialmedia.request.like;

import com.alseinn.socialmedia.entity.like.enums.ActionObjectEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeActionRequest {

    private long id;
    private ActionObjectEnum type;
    private String username;
}
