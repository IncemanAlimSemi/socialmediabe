package com.alseinn.socialmedia.request.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CreateCommentRequest {

    @NonNull
    private Long postId;
    @NonNull
    private String content;
    @NonNull
    private String username;
}
