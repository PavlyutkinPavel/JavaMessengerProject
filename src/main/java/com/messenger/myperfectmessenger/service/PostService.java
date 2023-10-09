package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Post;
import com.messenger.myperfectmessenger.exception.PostNotFoundException;
import com.messenger.myperfectmessenger.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPost(Long id) {
        return postRepository.findById(id);
    }

    public void createPost(Post post) {
        postRepository.save(post);
    }

    public void updatePost(Post post) {
        postRepository.saveAndFlush(post);
    }

    public void putLike(Long id) {
        Post post = getPost(id).orElseThrow(PostNotFoundException::new);
        post.setLikes(post.getLikes()+1);
        postRepository.saveAndFlush(post);
    }
    public void putDislike(Long id) {
        Post post = getPost(id).orElseThrow(PostNotFoundException::new);
        post.setDislikes(post.getDislikes()+1);
        postRepository.saveAndFlush(post);
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

}