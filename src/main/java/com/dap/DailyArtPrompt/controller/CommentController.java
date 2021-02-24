package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Comment;
import com.dap.DailyArtPrompt.model.CommentRequestBody;
import com.dap.DailyArtPrompt.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public Comment createComment(@Valid @RequestBody CommentRequestBody commentRequestBody) {
        log.info("Saving comment with the following requestBody: " + commentRequestBody);
        return commentService.createComment(commentRequestBody);
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable UUID id) {
        log.info("Deleting comment with the following id: " + id);
        commentService.deleteComment(id);
    }
    //TODO: update time
    @PutMapping("/comments/{id}")
    public Comment updateComment(@PathVariable UUID id, @RequestBody Comment comment) {
        log.info("Updating comment with id: " + id);
        return commentService.updateComment(comment);
    }

    @GetMapping("/comments")
    public List<Comment> getComments(@RequestParam UUID imageId) {
        log.info("Getting comments for imageId: " + imageId);
        return commentService.getComments(imageId);
    }
}
