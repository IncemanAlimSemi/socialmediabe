package com.alseinn.socialmedia.response.post;

import com.alseinn.socialmedia.response.comment.CommentDetailResponse;
import com.alseinn.socialmedia.response.concrete.AbstractResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponse extends AbstractResponse {
    String title;
    String content;
    Long like;
    Date date;
    List<CommentDetailResponse> commentDetailResponses;
}
