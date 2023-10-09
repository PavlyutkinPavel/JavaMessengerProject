package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findById(Long chatId);

    void deleteById(Long chatId);

//    @Modifying
//    @Query(nativeQuery = true, value = "DELETE FROM l_users_chats WHERE user_id =:userId AND chat_id =:chatId")
//    void removeUserByIdAndChatId(Long userId, Long chatId);
}
