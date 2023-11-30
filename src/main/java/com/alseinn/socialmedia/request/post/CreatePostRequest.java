package com.alseinn.socialmedia.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CreatePostRequest {
    @NotBlank(message = "{username.can.not.be.empty}")
    @Pattern(regexp = "^[a-zA-Z0-9-][a-zA-Z0-9.-]{2,14}$", message = "{username.validation.message}")
    private String username;
    @NotBlank(message = "{title.can.not.be.empty}")
    @Pattern(regexp = "^(.|\\n){1,150}$", message = "{title.validation.message}")
    private String title;
    @NotBlank(message = "{content.can.not.be.empty}")
    @Pattern(regexp = "^(.|\\n){1,1000}$", message = "{content.validation.message}")
    private String content;
}
