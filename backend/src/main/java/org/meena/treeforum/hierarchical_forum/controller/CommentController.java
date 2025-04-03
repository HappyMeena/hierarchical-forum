package org.meena.treeforum.hierarchical_forum.controller;

import org.meena.treeforum.hierarchical_forum.dto.CommentDto;
import org.meena.treeforum.hierarchical_forum.security.UserPrincipal;
import org.meena.treeforum.hierarchical_forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(commentService.addComment(commentDto, currentUser));
    }
}
