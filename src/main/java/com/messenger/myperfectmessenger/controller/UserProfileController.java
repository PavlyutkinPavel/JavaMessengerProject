package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.UserProfile;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController //для REST архитектуры
@RequestMapping("/user_profile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> getUserProfiles() {
        List<UserProfile> userProfiles = userProfileService.getUserProfiles();
        if (userProfiles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userProfiles, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long id) {
        UserProfile userProfile = userProfileService.getUserProfile(id).orElseThrow(UserNotFoundException::new);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createUserProfile(@RequestBody UserProfile userProfile) {
        userProfileService.createUserProfile(userProfile);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateUserProfile(@RequestBody UserProfile userProfile) {
        userProfileService.updateUserProfile(userProfile);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update_profile_image/{userId}")
    public ResponseEntity<String> updateProfileImage(@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {
        userProfileService.updateProfileImage(userId, file.getBytes());
        return new ResponseEntity<>("Profile image updated successfully", HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserProfile(@PathVariable Long id) {
        userProfileService.deleteUserProfileById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
