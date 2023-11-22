package com.alseinn.socialmedia.response.follow;

import com.alseinn.socialmedia.response.concrete.AbstractResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowDataResponse extends AbstractResponse {
    private String username;
}
