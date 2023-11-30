package com.alseinn.socialmedia.request.like;

import com.alseinn.socialmedia.entity.like.enums.ActionObjectEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeActionRequest {
    @NotNull(message = "{id.can.not.be.empty}")
    @Min(message = "{out.of.range}", value = 1)
    @Max(message = "{out.of.range}", value = Long.MAX_VALUE)
    private Long id;
    @NotNull(message = "{action.type.can.not.be.empty}")
    private ActionObjectEnum type;
    @NotBlank(message = "{username.can.not.be.empty}")
    @Pattern(regexp = "^[a-zA-Z0-9-][a-zA-Z0-9.-]{2,14}$", message = "{username.validation.message}")
    private String username;
}
