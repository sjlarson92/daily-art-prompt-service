package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.entity.Comment;
import com.dap.DailyArtPrompt.entity.User;
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
        UUID imageId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String text = "some comment";
        CommentRequestBody commentRequestBody = new CommentRequestBody(imageId, userId, text);
        @Test
        public void saveCommentWithFieldsMappedProperly() {
            User user = new User();
            user.setId(commentRequestBody.getUserId());
            commentService.createComment(commentRequestBody);
            verify(commentRepository).save(
                    argThat((myComment) ->
                            myComment.getImageId() == commentRequestBody.getImageId() &&
                            myComment.getUser().getId() == user.getId() &&
                            myComment.getText().equals(commentRequestBody.getText())
                    )
            );
        }

        @Test
        public void returnSavedComment() {
            User user = new User();
            user.setId(commentRequestBody.getUserId());
            Comment comment = new Comment(UUID.randomUUID(), imageId, user, text, OffsetDateTime.now(), null);
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

    @Nested
    class deleteComment {
        @Test
        public void callsRepoWithId() {
            UUID id = UUID.randomUUID();
            commentService.deleteComment(id);
            verify(commentRepository).deleteById(id);
        }
    }

    @Nested
    class updateComment {
        @Test
        public void returnsUpdatedComment() {
            UUID id = UUID.randomUUID();
            UUID imageId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            String text = "some comment";
            Comment comment = new Comment(id, imageId, null, text, null, null);
            when(commentRepository.save(comment)).thenReturn(comment);
            Comment updatedComment = commentService.updateComment(comment);
            assertThat(updatedComment).isEqualTo(comment);
        }
    }
}
