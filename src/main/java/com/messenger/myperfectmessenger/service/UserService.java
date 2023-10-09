package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.repository.UserRepository;
import com.messenger.myperfectmessenger.security.domain.SecurityCredentials;
import com.messenger.myperfectmessenger.security.repository.SecurityCredentialsRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityCredentialsRepository securityCredentialsRepository;

    public UserService(UserRepository userRepository, SecurityCredentialsRepository securityCredentialsRepository) {
        this.userRepository = userRepository;
        this.securityCredentialsRepository = securityCredentialsRepository;
    }

    public List<User> getUsers(Principal principal) {
        return userRepository.findAll(Sort.by("id"));
    }

    public Optional<User> findUserByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    public Optional<User> findUserByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public User getUser(Long id, Principal principal) {
        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(principal.getName()).orElseThrow(UserNotFoundException::new);
        Long currentUserId = credentials.getUserId();
        User user =  userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Role role = credentials.getUserRole();
        if((currentUserId == user.getId()) || (role.toString() == "ADMIN")){
            return user;
        }else{
            return null;
        }
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
