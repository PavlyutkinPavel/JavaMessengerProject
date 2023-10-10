package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.Comment;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.CommentService;
import com.messenger.myperfectmessenger.exception.CommentNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Tag(name = "Comment Controller", description = "makes all operations with comments")
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final SecurityService securityService;

    private static HashMap<String, Long> statistics = new HashMap<>();

    public CommentController(CommentService commentService, SecurityService securityService) {
        this.commentService = commentService;
        this.securityService = securityService;
    }

    @Operation(summary = "get all comments(for all authorized users)")
    @GetMapping
    public ResponseEntity<List<Comment>> getComments() {
        List<Comment> comments = commentService.getComments();
        if (comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }

    @Operation(summary = "get specific comment(for all authorized users)")
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        Comment comment = commentService.getComment(id).orElseThrow(CommentNotFoundException::new);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @Operation(summary = "create comment(for all authorized users)")
    @PostMapping
    public ResponseEntity<HttpStatus> createComment(@RequestBody Comment comment) {
        commentService.createComment(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "update comments(for all admins and comment's author)")
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateComment(@PathVariable Long id, @RequestBody Comment comment, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || (comment.getUserId() == securityService.getUserIdByLogin(principal.getName()))){
            commentService.updateComment(comment);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "delete comments(for all admins and comment's author)")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable Long id, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || (getComment(id).getBody().getUserId() == securityService.getUserIdByLogin(principal.getName()))){
            commentService.deleteCommentById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @Operation(summary = "get comment statistic (for all authorized users)")
    @GetMapping("/statistics/{id}")
    public ResponseEntity<HashMap<String, Long>> getCommentStatistics(@PathVariable Long id) {
        Comment comment = commentService.getComment(id).orElseThrow(CommentNotFoundException::new);

        statistics.put("Likes: ", comment.getLikes());
        statistics.put("Dislikes: ", comment.getDislikes());

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @Operation(summary = "put like to comment(for all users)")
    @PutMapping("/like/{id}")
    public ResponseEntity<HashMap<String, Long>> putLike(@PathVariable Long id) {
        commentService.putLike(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "put dislike to comment(for all users)")
    @PutMapping("/dislike/{id}")
    public ResponseEntity<HashMap<String, Long>> putDislike(@PathVariable Long id) {
        commentService.putDislike(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
