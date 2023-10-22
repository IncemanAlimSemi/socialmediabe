package com.alseinn.socialmedia.response.user;

import com.alseinn.socialmedia.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetFollowersResponse {

    private Set<UserFollowersDataResponse> followers;
}
