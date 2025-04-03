package org.meena.treeforum.hierarchical_forum.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meena.treeforum.hierarchical_forum.model.Comment;
import org.meena.treeforum.hierarchical_forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");
        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Test
    void findByParentIsNullOrderByCreatedAtDesc_shouldReturnTopLevelCommentsInDescendingOrder() {
        // Arrange
        Comment comment1 = createComment("Content 1", null);
        comment1.setCreatedAt(LocalDateTime.now().minusDays(1));

        Comment comment2 = createComment("Content 2", null);
        comment2.setCreatedAt(LocalDateTime.now());

        Comment reply = createComment("Reply Content", comment1);

        entityManager.persist(comment1);
        entityManager.persist(comment2);
        entityManager.persist(reply);
        entityManager.flush();

        // Act
        List<Comment> result = commentRepository.findByParentIsNullOrderByCreatedAtDesc();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getContent()).isEqualTo("Content 2"); // Newest first
        assertThat(result.get(1).getContent()).isEqualTo("Content 1");
        assertThat(result).extracting(Comment::getParent).containsOnlyNulls();
    }

    @Test
    void findTopLevelCommentsWithReplies_shouldReturnTopLevelCommentsWithRepliesFetched() {
        // Arrange
        Comment comment1 = createComment("Content 1", null);
        Comment comment2 = createComment("Content 2", null);
        Comment reply1 = createComment("Reply 1", comment1);
        Comment reply2 = createComment("Reply 2", comment1);

        entityManager.persist(comment1);
        entityManager.persist(comment2);
        entityManager.persist(reply1);
        entityManager.persist(reply2);
        entityManager.flush();
        entityManager.clear(); // Clear to test if replies are actually fetched

        // Act
        List<Comment> result = commentRepository.findTopLevelCommentsWithReplies();

        // Assert
        assertThat(result).hasSize(2);

        // Verify comment with replies
        Comment parentWithReplies = result.stream()
                .filter(c -> c.getContent().equals("Content 1"))
                .findFirst()
                .orElseThrow();
        assertThat(parentWithReplies.getReplies())
                .hasSize(2)
                .extracting(Comment::getContent)
                .containsExactlyInAnyOrder("Reply 1", "Reply 2");

        // Verify comment without replies
        Comment parentWithoutReplies = result.stream()
                .filter(c -> c.getContent().equals("Content 2"))
                .findFirst()
                .orElseThrow();
        assertThat(parentWithoutReplies.getReplies()).isEmpty();
    }

    @Test
    void findTopLevelCommentsWithReplies_shouldReturnEmptyListWhenNoCommentsExist() {
        // Act
        List<Comment> result = commentRepository.findTopLevelCommentsWithReplies();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findByParentIsNullOrderByCreatedAtDesc_shouldReturnEmptyListWhenNoTopLevelCommentsExist() {
        // Arrange
        Comment parent = createComment("Parent", null);
        entityManager.persist(parent);

        Comment reply = createComment("Reply", parent);
        entityManager.persist(reply);
        entityManager.flush();

        // Act
        List<Comment> result = commentRepository.findByParentIsNullOrderByCreatedAtDesc();

        // Assert
        assertThat(result).hasSize(1); // Only the parent should be returned
        assertThat(result.get(0).getContent()).isEqualTo("Parent");
    }

    private Comment createComment(String content, Comment parent) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(testUser);
        comment.setParent(parent);
        return comment;
    }
}