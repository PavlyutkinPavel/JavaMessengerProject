package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.UserProfile;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.repository.UserProfileRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfileRepository.findAll(Sort.by("id"));
    }

    public Optional<UserProfile> getUserProfile(Long id) {
        //проверка на логин
        return userProfileRepository.findById(id);
    }
    public void createUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    public void updateUserProfile(UserProfile userProfile) {
        userProfileRepository.saveAndFlush(userProfile);
    }

    public void updateProfileImage(Long userId, byte[] profileImage) {
        UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userProfile.setProfileImage(profileImage);
        userProfileRepository.saveAndFlush(userProfile);
    }

    public void deleteUserProfileById(Long id){
        userProfileRepository.deleteById(id);
    }
}
