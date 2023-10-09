package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.Comment;
import com.messenger.myperfectmessenger.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);
}
