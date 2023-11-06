package com.alseinn.socialmedia.controller.comment;

import com.alseinn.socialmedia.request.comment.CreateCommentRequest;
import com.alseinn.socialmedia.request.comment.DeleteCommentRequest;
import com.alseinn.socialmedia.response.comment.CommentResponse;
import com.alseinn.socialmedia.response.post.PostResponse;
import com.alseinn.socialmedia.service.comment.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public CommentResponse createComment(@RequestBody CreateCommentRequest createCommentRequest) throws JsonProcessingException {
        return commentService.createComment(createCommentRequest);
    }

    @DeleteMapping("/delete")
    public CommentResponse deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest) throws JsonProcessingException {
        return commentService.deleteComment(deleteCommentRequest);
    }
}
