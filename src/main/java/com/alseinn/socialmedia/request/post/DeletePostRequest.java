package com.alseinn.socialmedia.request.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DeletePostRequest {

    @NonNull
    private Long postId;
    @NonNull
    private String username;
}
