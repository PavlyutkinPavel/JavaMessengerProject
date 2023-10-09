package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.Chat;
import com.messenger.myperfectmessenger.domain.UserChatRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserChatRelationRepository extends JpaRepository<UserChatRelation, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM l_users_chats WHERE user_id = :userId AND chat_id = :chatId", nativeQuery = true)
    void removeUserByIdAndChatId(@Param("userId") Long userId, @Param("chatId") Long chatId);

    @Query(nativeQuery = true, value = "SELECT c.* FROM chats c " +
            "JOIN l_users_chats luc ON c.id = luc.chat_id " +
            "WHERE luc.user_id = :userId")
    List<Object[]> findAllChatByUserId(Long userId);

}
