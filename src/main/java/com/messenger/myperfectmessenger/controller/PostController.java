package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.Post;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.PostService;
import com.messenger.myperfectmessenger.exception.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final SecurityService securityService;

    private static HashMap<String, Long> statistics = new HashMap<>();

    public PostController(PostService postService, SecurityService securityService) {
        this.postService = postService;
        this.securityService = securityService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Principal principal) {
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
    public ResponseEntity<HttpStatus> updatePost(@RequestBody Post post, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || (post.getUserId() == securityService.getUserIdByLogin(principal.getName()))){
            postService.updatePost(post);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long id, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || (getPost(id).getBody().getUserId() == securityService.getUserIdByLogin(principal.getName()))){
            postService.deletePostById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/statistics/{id}")
    public ResponseEntity<HashMap<String, Long>> getPostStatistics(@PathVariable Long id) {
        Post post = postService.getPost(id).orElseThrow(PostNotFoundException::new);

        statistics.put("Likes: ", post.getLikes());
        statistics.put("Dislikes: ", post.getDislikes());
        statistics.put("Comments: ", post.getComments());

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
    @PutMapping("/like/{id}")
    public ResponseEntity<HashMap<String, Long>> putLike(@PathVariable Long id) {
        postService.putLike(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/dislike/{id}")
    public ResponseEntity<HashMap<String, Long>> putDislike(@PathVariable Long id) {
        postService.putDislike(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}