package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.UserFriendRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFriendRelationRepository extends JpaRepository<UserFriendRelation, Long> {
    Optional<UserFriendRelation> findByUser_IdAndFriend_Id(Long userId, Long friendId);
}
