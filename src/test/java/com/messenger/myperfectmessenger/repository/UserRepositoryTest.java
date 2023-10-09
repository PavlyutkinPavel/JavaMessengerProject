package com.messenger.myperfectmessenger.repository;


import com.messenger.myperfectmessenger.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    static User userInfo;

    @BeforeAll
    static void beforeAll() {
        userInfo = new User();
        userInfo.setFirstName("TestFirstName");
        userInfo.setLastName("TestLastName");
    }

    @Test
    void findAllTest() {
        userRepository.save(userInfo);
        List<User> newList = userRepository.findAll();
        Assertions.assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        User saved = userRepository.save(userInfo);
        Optional<User> newUser = userRepository.findById(saved.getId());
        Assertions.assertTrue(newUser.isPresent());
    }

    @Test
    void saveTest() {
        List<User> oldList = userRepository.findAll();
        userRepository.save(userInfo);
        List<User> newList = userRepository.findAll();
        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateTest() {
        User userSaved = userRepository.save(userInfo);
        userSaved.setFirstName("UPDATED_NAME");
        User userUpdated = userRepository.saveAndFlush(userSaved);
        Assertions.assertEquals(userUpdated.getFirstName(),"UPDATED_NAME");
    }

    @Test
    void deleteTest() {
        User userSaved = userRepository.save(userInfo);
        userRepository.delete(userSaved);
        Optional<User> user = userRepository.findById(userSaved.getId());
        Assertions.assertFalse(user.isPresent());
    }
}