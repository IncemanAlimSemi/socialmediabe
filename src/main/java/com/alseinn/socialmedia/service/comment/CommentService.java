package com.alseinn.socialmedia.service.comment;

import com.alseinn.socialmedia.entity.comment.Comment;
import com.alseinn.socialmedia.request.comment.CreateCommentRequest;
import com.alseinn.socialmedia.request.comment.DeleteCommentRequest;
import com.alseinn.socialmedia.response.comment.CommentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CommentService {

    CommentResponse createComment(CreateCommentRequest createCommentRequest) throws JsonProcessingException;

    CommentResponse deleteComment(DeleteCommentRequest deleteCommentRequest) throws JsonProcessingException;

    Comment findById(Long id);
}
