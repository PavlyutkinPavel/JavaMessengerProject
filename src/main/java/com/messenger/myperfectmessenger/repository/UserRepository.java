package com.messenger.myperfectmessenger.repository;

import com.messenger.myperfectmessenger.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM user_info WHERE last_name = :ln")
    Optional<User> findByLastName(String ln);

    @Query(nativeQuery = true, value = "SELECT * FROM user_info WHERE first_name = :fn")
    Optional<User> findByFirstName(String fn);

    Optional<User> findById(Long userId);

    void deleteById(Long userId);

}