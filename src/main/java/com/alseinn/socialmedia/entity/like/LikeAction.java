package com.alseinn.socialmedia.entity.like;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "like_action")
public class LikeAction {
    @EmbeddedId
    private LikeActionKey id;
}
