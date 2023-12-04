package com.alseinn.socialmedia.entity.post;

import com.alseinn.socialmedia.entity.comment.Comment;
import com.alseinn.socialmedia.entity.item.Item;
import com.alseinn.socialmedia.entity.listeners.ModifiedDateEntityListener;
import com.alseinn.socialmedia.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ModifiedDateEntityListener.class)
@Entity
@Table(name = "post")
public class Post extends Item {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;
    @Column(nullable = false)
    private long postLike;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Column(nullable = false)
    private List<Comment> comments;
}
