package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.FriendsList;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.domain.UserProfile;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Tag(name = "UserProfile Controller", description = "Make all operations with your profile")
@RestController //для REST архитектуры
@RequestMapping("/user_profile")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final SecurityService securityService;

    private final Path ROOT_FILE_PATH = Paths.get("src/main/resources/static/images");

    public UserProfileController(UserProfileService userProfileService, SecurityService securityService) {
        this.userProfileService = userProfileService;
        this.securityService = securityService;
    }

    @Operation(summary = "get all profiles(for admins)")
    @GetMapping
    public ResponseEntity<List<UserProfile>> getUserProfiles(Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            List<UserProfile> userProfiles = userProfileService.getUserProfiles();
            if (userProfiles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(userProfiles, HttpStatus.OK);
            }
        } else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "get your user profile")
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long id, Principal principal) {
        UserProfile userProfile = userProfileService.getUserProfile(id, principal);
        if(userProfile != null){
            return new ResponseEntity<>(userProfile, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "get your friend user profile")
    @GetMapping("/friend/{id}")
    public ResponseEntity<UserProfile> getFriendUserProfile(@PathVariable Long id, Principal principal) {
        UserProfile userProfile = userProfileService.getFriendUserProfile(id, principal).orElseThrow(UserNotFoundException::new);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @Operation(summary = "create user profile")
    @PostMapping
    public ResponseEntity<HttpStatus> createUserProfile(@RequestBody UserProfile userProfile) {
        userProfileService.createUserProfile(userProfile);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "update your user profile")
    @PutMapping
    public ResponseEntity<HttpStatus> updateUserProfile(@RequestBody UserProfile userProfile, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || (securityService.getUserIdByLogin(principal.getName())==userProfile.getUserId())){
            userProfileService.updateUserProfile(userProfile);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "update your user profile picture")
    @PutMapping("/upload/{id}")
    public ResponseEntity<HttpStatus> updateProfileImage(@PathVariable Long id, @RequestParam("file") MultipartFile file, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || (securityService.getUserIdByLogin(principal.getName())==id)){
            try {
                UserProfile userProfile = userProfileService.getUserProfile(id, principal);
                userProfile.setProfileImage(Files.copy(file.getInputStream(), this.ROOT_FILE_PATH.resolve(file.getOriginalFilename())));
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (IOException e) {
                System.out.println(e);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "delete your user profile")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserProfile(@PathVariable Long id, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || (securityService.getUserIdByLogin(principal.getName())==userProfileService.getUserProfile(id, principal).getUserId())){
            userProfileService.deleteUserProfileById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
