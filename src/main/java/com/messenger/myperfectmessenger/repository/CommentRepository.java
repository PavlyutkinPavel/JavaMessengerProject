package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
