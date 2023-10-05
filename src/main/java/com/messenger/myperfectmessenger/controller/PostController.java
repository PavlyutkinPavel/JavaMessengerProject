package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.Post;
import com.messenger.myperfectmessenger.service.PostService;
import com.messenger.myperfectmessenger.exception.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    private static HashMap<String, Long> statistics = new HashMap<>();

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.getPosts();
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        Post post = postService.getPost(id).orElseThrow(PostNotFoundException::new);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createPost(@RequestBody Post post) {
        postService.createPost(post);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updatePost(@RequestBody Post post) {
        postService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/statistics/{id}")
    public ResponseEntity<HashMap<String, Long>> getPostStatistics(@PathVariable Long id) {
        Post post = postService.getPost(id).orElseThrow(PostNotFoundException::new);

        statistics.put("Likes: ", post.getLikes());
        statistics.put("Dislikes: ", post.getDislikes());
        statistics.put("Comments: ", post.getComments());

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}