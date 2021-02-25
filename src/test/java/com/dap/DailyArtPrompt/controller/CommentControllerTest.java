package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Comment;
import com.dap.DailyArtPrompt.model.CommentRequestBody;
import com.dap.DailyArtPrompt.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @Nested
    class createComment {

        @Nested
        class givenValidCommentRequestBody {
            @Test
            public void callsCreateCommentWithCorrectParam() throws Exception {
                CommentRequestBody commentRequestBody = new CommentRequestBody(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "comment"
                );
                mockMvc.perform(post("/comments")
                        .content(objectMapper.writeValueAsString(commentRequestBody))
                        .contentType(MediaType.APPLICATION_JSON));
                verify(commentService).createComment(commentRequestBody);
            }
        }

        @Nested
        class givenInvalidCommentRequestBody {
            @Test
            public void return400() throws Exception {
                CommentRequestBody commentRequestBody = new CommentRequestBody();
                mockMvc.perform(post("/comments")
                        .content(objectMapper.writeValueAsString(commentRequestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
                verify(commentService, times(0)).createComment(commentRequestBody);
            }
        }
    }

    @Nested
    class deleteComment {
        @Test
        public void callsCommentServiceWithId() throws Exception {
            UUID commentId = UUID.randomUUID();
            mockMvc.perform(delete("/comments/" + commentId));
            verify(commentService).deleteComment(commentId);

        }
    }

    @Nested
    class updateComment {
        @Test
        public void callsCommentServiceWithComment() throws Exception {
            UUID id = UUID.randomUUID();
            UUID imageId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            Comment comment = new Comment(id, imageId, userId, "comment", null, null);
            mockMvc.perform(put("/comments/" + id)
                    .content(objectMapper.writeValueAsString(comment))
                    .contentType(MediaType.APPLICATION_JSON)
            );
            verify(commentService).updateComment(comment);

        }
    }

    @Nested
    class getComments {
        @Test
        public void returnsCommentsForGivenImageId() throws Exception {
            UUID imageId = UUID.randomUUID();
            when(commentService.getComments(imageId)).thenReturn(Collections.emptyList());
            mockMvc.perform(get("/comments?imageId=" + imageId))
                    .andExpect(content().string("[]"));
        }
    }
}
