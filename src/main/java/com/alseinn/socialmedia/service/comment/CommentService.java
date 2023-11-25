package com.alseinn.socialmedia.service.comment;

import com.alseinn.socialmedia.entity.comment.Comment;
import com.alseinn.socialmedia.request.comment.CreateCommentRequest;
import com.alseinn.socialmedia.request.comment.DeleteCommentRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;

import java.io.IOException;

public interface CommentService {

    GeneralInformationResponse createComment(CreateCommentRequest createCommentRequest) throws IOException;

    GeneralInformationResponse deleteComment(DeleteCommentRequest deleteCommentRequest) throws IOException;

    Comment findById(Long id);
}
