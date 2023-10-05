package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
