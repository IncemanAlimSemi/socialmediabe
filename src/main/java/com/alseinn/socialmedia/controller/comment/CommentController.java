package com.alseinn.socialmedia.controller.comment;

import com.alseinn.socialmedia.request.comment.CreateCommentRequest;
import com.alseinn.socialmedia.request.comment.DeleteCommentRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<GeneralInformationResponse> createComment(@RequestBody CreateCommentRequest createCommentRequest) throws IOException {
        return ResponseEntity.ok(commentService.createComment(createCommentRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GeneralInformationResponse> deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest) throws IOException {
        return ResponseEntity.ok(commentService.deleteComment(deleteCommentRequest));
    }
}
