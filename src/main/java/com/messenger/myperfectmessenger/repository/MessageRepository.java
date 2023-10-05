package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    void deleteAll();
}
