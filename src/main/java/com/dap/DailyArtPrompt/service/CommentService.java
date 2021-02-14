package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Comment;
import com.dap.DailyArtPrompt.model.CommentRequestBody;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommentService {
    public void createComment(CommentRequestBody commentRequestBody) {
        Comment comment = new Comment(
                UUID.randomUUID(),
                commentRequestBody.getImageId(),
                commentRequestBody.getUserId(),
                commentRequestBody.getText()
        );
    }
}
