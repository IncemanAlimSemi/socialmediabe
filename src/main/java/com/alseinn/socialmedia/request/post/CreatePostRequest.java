package com.alseinn.socialmedia.request.post;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CreatePostRequest {

    @Nonnull
    private String username;
    @NonNull
    private String title;
    @NonNull
    private String content;

}
