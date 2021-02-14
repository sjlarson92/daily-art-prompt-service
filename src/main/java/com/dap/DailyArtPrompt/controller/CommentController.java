package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.model.CommentRequestBody;
import com.dap.DailyArtPrompt.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public void createComment(@RequestBody CommentRequestBody commentRequestBody) {
        commentService.createComment(commentRequestBody);
    }

    @GetMapping("/comments")
    public void getComments() {
        // TODO: intake imageId get all comments for image
    }

    @PutMapping("/comments/{id}")
    public void updateComment() {

    }

    @DeleteMapping("/comments/{id}")
    public void deleteComment() {

    }
}
