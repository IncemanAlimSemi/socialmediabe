package com.alseinn.socialmedia.entity.like;

import com.alseinn.socialmedia.entity.item.Item;
import com.alseinn.socialmedia.entity.like.enums.ActionObjectEnum;
import com.alseinn.socialmedia.entity.listeners.ModifiedDateEntityListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ModifiedDateEntityListener.class)
@Entity
@Table(name = "like_action", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username", "actionObject","actionObjectId"})
})
public class LikeAction extends Item {
    private String username;
    private ActionObjectEnum actionObject;
    private long actionObjectId;
}
