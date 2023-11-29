package com.alseinn.socialmedia.controller.comment;

import com.alseinn.socialmedia.request.comment.CreateCommentRequest;
import com.alseinn.socialmedia.request.comment.DeleteCommentRequest;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${default.api.path}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<GeneralInformationResponse> createComment(
            @Valid @RequestBody CreateCommentRequest createCommentRequest,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ) throws IOException {
        return ResponseEntity.ok(commentService.createComment(createCommentRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GeneralInformationResponse> deleteComment(
            @Valid @RequestBody DeleteCommentRequest deleteCommentRequest,
            @SuppressWarnings(value = "unused") BindingResult bindingResult
    ) throws IOException {
        return ResponseEntity.ok(commentService.deleteComment(deleteCommentRequest));
    }
}
