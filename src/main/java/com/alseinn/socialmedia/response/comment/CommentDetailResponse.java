package com.alseinn.socialmedia.response.comment;

import com.alseinn.socialmedia.response.concrete.AbstractResponse;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetailResponse extends AbstractResponse {
    String username;
    String content;
    Date date;
}
