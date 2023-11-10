package com.alseinn.socialmedia.entity.like;

import com.alseinn.socialmedia.entity.like.enums.ActionObjectEnum;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeActionKey implements Serializable {

    private String username;
    private ActionObjectEnum actionObject;
    private long actionObjectId;
}
