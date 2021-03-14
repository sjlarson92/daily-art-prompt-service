package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Comment;
import com.dap.DailyArtPrompt.entity.User;
import com.dap.DailyArtPrompt.model.CommentRequestBody;
import com.dap.DailyArtPrompt.repository.CommentRepository;
import com.dap.DailyArtPrompt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public Comment createComment(CommentRequestBody commentRequestBody) {
        Optional<User> existingUser = userRepository.findById(commentRequestBody.getUserId());
        existingUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found by id: " + commentRequestBody.getUserId()));

        Comment comment = new Comment(
                UUID.randomUUID(),
                commentRequestBody.getImageId(),
                existingUser.get(),
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
