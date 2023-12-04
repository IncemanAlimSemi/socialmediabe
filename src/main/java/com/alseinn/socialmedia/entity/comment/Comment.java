package com.alseinn.socialmedia.entity.comment;

import com.alseinn.socialmedia.entity.item.Item;
import com.alseinn.socialmedia.entity.listeners.ModifiedDateEntityListener;
import com.alseinn.socialmedia.entity.post.Post;
import com.alseinn.socialmedia.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ModifiedDateEntityListener.class)
@Entity
@Table(name = "comment")
public class Comment extends Item {
    @Column(nullable = false)
    private String content;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    @Column(nullable = false)
    private Date date;
}
