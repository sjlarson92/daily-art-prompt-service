package com.dap.DailyArtPrompt.repository;

import com.dap.DailyArtPrompt.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> getCommentsByImageId(UUID imageId);

    @Transactional
    void deleteAllByImageId(UUID imageId);
}
