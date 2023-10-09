package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.FriendRequest;
import com.messenger.myperfectmessenger.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findById(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM friend_requests WHERE sender = :username")
    List<FriendRequest> findAllByUsername(String username);
}
