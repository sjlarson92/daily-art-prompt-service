package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Comment;
import com.dap.DailyArtPrompt.model.CommentRequestBody;
import com.dap.DailyArtPrompt.repository.CommentRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    @Nested
    class createComment {

        @Test
        public void saveCommentWithFieldsMappedProperly() {
            CommentRequestBody commentRequestBody = new CommentRequestBody(UUID.randomUUID(), UUID.randomUUID(), "comment");
            commentService.createComment(commentRequestBody);
            verify(commentRepository).save(
                    argThat((myComment) ->
                            myComment.getImageId() == commentRequestBody.getImageId() &&
                            myComment.getUserId() == commentRequestBody.getUserId() &&
                            myComment.getText().equals(commentRequestBody.getText())
                    )
            );
        }

        @Test
        public void returnSavedComment() {
            UUID imageId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            String text = "some comment";
            CommentRequestBody commentRequestBody = new CommentRequestBody(imageId, userId, text);
            Comment comment = new Comment(UUID.randomUUID(), imageId, userId, text, OffsetDateTime.now());
            when(commentRepository.save(any(Comment.class))).thenReturn(comment);
            Comment savedComment = commentService.createComment(commentRequestBody);
            assertThat(savedComment).isEqualTo(comment);
        }
    }

    @Nested
    class getComments {
        @Test
        public void returnsListOfComments() {
            UUID imageId = UUID.randomUUID();
            when(commentRepository.getCommentsByImageId(imageId)).thenReturn(Collections.emptyList());
            List<Comment> comments = commentService.getComments(imageId);
            assertThat(comments).isEqualTo(Collections.emptyList());
        }
    }
}
