package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findById(Long chatId);

    void deleteById(Long chatId);
}
