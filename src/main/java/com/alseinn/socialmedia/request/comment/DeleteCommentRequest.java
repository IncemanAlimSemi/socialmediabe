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
public class DeleteCommentRequest {

    @NonNull
    private Long commentId;
    @NonNull
    private String username;
}
