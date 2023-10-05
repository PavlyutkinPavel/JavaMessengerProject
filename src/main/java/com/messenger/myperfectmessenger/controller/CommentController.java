package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.Comment;
import com.messenger.myperfectmessenger.service.CommentService;
import com.messenger.myperfectmessenger.exception.CommentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    private static HashMap<String, Long> statistics = new HashMap<>();

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments() {
        List<Comment> comments = commentService.getComments();
        if (comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        Comment comment = commentService.getComment(id).orElseThrow(CommentNotFoundException::new);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createComment(@RequestBody Comment comment) {
        commentService.createComment(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        comment.setId(id);
        commentService.updateComment(comment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable Long id) {
        commentService.deleteCommentById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/statistics/{id}")
    public ResponseEntity<HashMap<String, Long>> getCommentStatistics(@PathVariable Long id) {
        Comment comment = commentService.getComment(id).orElseThrow(CommentNotFoundException::new);

        statistics.put("Likes: ", comment.getLikes());
        statistics.put("Dislikes: ", comment.getDislikes());

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

}
