package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Comment;
import com.dap.DailyArtPrompt.model.CommentRequestBody;
import com.dap.DailyArtPrompt.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createComment(CommentRequestBody commentRequestBody) {
        Comment comment = new Comment(
                UUID.randomUUID(),
                commentRequestBody.getImageId(),
                commentRequestBody.getUserId(),
                commentRequestBody.getText(),
                OffsetDateTime.now()
        );
        return commentRepository.save(comment);
    }
}