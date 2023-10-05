package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll(Sort.by("id"));
    }

    public Optional<User> findUserByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    public Optional<User> findUserByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public Optional<User> getUser(Long id) {
        //проверка на логин
        return userRepository.findById(id);
    }
    public void createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }
}
