package org.meena.treeforum.hierarchical_forum.repository;

import org.meena.treeforum.hierarchical_forum.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByParentIsNullOrderByCreatedAtDesc();

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.replies WHERE c.parent IS NULL ORDER BY c.createdAt DESC")
    List<Comment> findTopLevelCommentsWithReplies();
}
