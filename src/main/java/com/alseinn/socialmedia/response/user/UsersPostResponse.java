package com.alseinn.socialmedia.response.user;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.response.post.PostDetailResponse;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersPostResponse extends GeneralInformationResponse {
    String username;
    List<PostDetailResponse> postDetailResponses;

    @Builder(builderMethodName = "builderWithExtendFields")
    @SuppressWarnings(value = "unused")
    public UsersPostResponse(boolean isSuccess, String message, String username, List<PostDetailResponse> postDetailResponses) {
        super(isSuccess, message);
        this.username = username;
        this.postDetailResponses = postDetailResponses;
    }
}
