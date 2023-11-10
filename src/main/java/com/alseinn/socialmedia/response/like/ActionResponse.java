package com.alseinn.socialmedia.response.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionResponse {

    private boolean isSuccess;
    private String message;
}
