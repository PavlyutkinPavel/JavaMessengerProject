package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
    void deleteById(Long userId);
}
