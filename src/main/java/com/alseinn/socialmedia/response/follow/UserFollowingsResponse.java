package com.alseinn.socialmedia.response.follow;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowingsResponse extends GeneralInformationResponse {
    private Set<FollowDataResponse> followings;
}
