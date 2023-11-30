package com.alseinn.socialmedia.request.follow;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowRequest {
    @NotBlank(message = "{username.can.not.be.empty}")
    @Pattern(regexp = "^[a-zA-Z0-9-][a-zA-Z0-9.-]{2,14}$", message = "{username.validation.message}")
    private String follow;
}
