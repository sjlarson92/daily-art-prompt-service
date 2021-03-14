package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Comment;
import com.dap.DailyArtPrompt.entity.User;
import com.dap.DailyArtPrompt.model.CommentRequestBody;
import com.dap.DailyArtPrompt.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createComment(CommentRequestBody commentRequestBody) {
        User user = new User();
        user.setId(commentRequestBody.getUserId());
        Comment comment = new Comment(
                UUID.randomUUID(),
                commentRequestBody.getImageId(),
                user,
                commentRequestBody.getText(),
                OffsetDateTime.now(),
                null
        );
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(UUID imageId) {
        return commentRepository.getCommentsByImageId(imageId);
    }

    public void deleteComment(UUID id) {
        commentRepository.deleteById(id);
    }

    public Comment updateComment(Comment comment) {
        comment.setUpdatedAt(OffsetDateTime.now());
        return commentRepository.save(comment);
    }
}
