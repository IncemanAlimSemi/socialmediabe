package com.alseinn.socialmedia.response.comment;

import com.alseinn.socialmedia.response.concrete.AbstractResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetailResponse extends AbstractResponse {
    String username;
    String content;
    Date date;
}
