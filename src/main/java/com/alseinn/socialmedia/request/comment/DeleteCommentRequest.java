package com.alseinn.socialmedia.request.comment;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCommentRequest {
    @NotNull(message = "{id.can.not.be.empty}")
    @Min(message = "{out.of.range}", value = 1)
    @Max(message = "{out.of.range}", value = Long.MAX_VALUE)
    private Long id;
    @NotBlank(message = "{username.can.not.be.empty}")
    @Pattern(regexp = "^[a-zA-Z0-9-][a-zA-Z0-9.-]{2,14}$", message = "{username.validation.message}")
    private String username;
}
