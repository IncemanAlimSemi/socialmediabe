package com.alseinn.socialmedia.response.user;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.response.post.PostDetailResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UsersPostResponse extends GeneralInformationResponse {
    String username;
    List<PostDetailResponse> postDetailResponses;
}
