package org.meena.treeforum.hierarchical_forum.service;

import org.meena.treeforum.hierarchical_forum.dto.CommentDto;
import org.meena.treeforum.hierarchical_forum.model.Comment;
import org.meena.treeforum.hierarchical_forum.model.User;
import org.meena.treeforum.hierarchical_forum.repository.CommentRepository;
import org.meena.treeforum.hierarchical_forum.repository.UserRepository;
import org.meena.treeforum.hierarchical_forum.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CommentDto> getAllComments() {
        List<Comment> comments = commentRepository.findTopLevelCommentsWithReplies();
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CommentDto addComment(CommentDto commentDto, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        if (commentDto.getParentId() != null) {
            Comment parent = commentRepository.findById(commentDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParent(parent);
        }

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUsername(comment.getUser().getUsername());
        dto.setUserId(comment.getUser().getId());

        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            dto.setReplies(comment.getReplies().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
