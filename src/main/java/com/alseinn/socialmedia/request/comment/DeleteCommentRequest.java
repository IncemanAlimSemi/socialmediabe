package com.alseinn.socialmedia.request.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCommentRequest {

    @NonNull
    private Long id;
    @NonNull
    private String username;
}
