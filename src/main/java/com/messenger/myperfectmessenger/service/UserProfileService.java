package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.domain.UserProfile;
import com.messenger.myperfectmessenger.exception.FriendNotFoundException;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.repository.UserFriendRelationRepository;
import com.messenger.myperfectmessenger.repository.UserProfileRepository;
import com.messenger.myperfectmessenger.security.domain.SecurityCredentials;
import com.messenger.myperfectmessenger.security.repository.SecurityCredentialsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final SecurityCredentialsRepository securityCredentialsRepository;

    private UserFriendRelationRepository userFriendRelationRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, SecurityCredentialsRepository securityCredentialsRepository, UserFriendRelationRepository userFriendRelationRepository) {
        this.userProfileRepository = userProfileRepository;
        this.securityCredentialsRepository = securityCredentialsRepository;
        this.userFriendRelationRepository = userFriendRelationRepository;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfileRepository.findAll(Sort.by("id"));
    }

    public UserProfile getUserProfile(Long id, Principal principal) {
        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(principal.getName()).orElseThrow(UserNotFoundException::new);
        Long currentUserId = credentials.getUserId();
        UserProfile userProfile =  userProfileRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Role role = credentials.getUserRole();
        if((currentUserId == userProfile.getUserId()) ||(role.toString() == "ADMIN")){
            return userProfile;
        }else{
            return null;
        }
    }

    public Optional<UserProfile> getFriendUserProfile(Long id,Principal principal){
        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(principal.getName()).orElseThrow(UserNotFoundException::new);
        Long currentUserId = credentials.getUserId();
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Long friendId = userProfile.getUserId();
        userFriendRelationRepository.findByUser_IdAndFriend_Id(currentUserId, friendId).orElseThrow(FriendNotFoundException::new);
        return userProfileRepository.findById(id);
    }
    public void createUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    public void updateUserProfile(UserProfile userProfile) {
        userProfileRepository.saveAndFlush(userProfile);
    }

//    @Transactional
//    public void updateProfileImage(Long userId, byte[] profileImage) {
//        UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(UserNotFoundException::new);
//        if (userProfile != null) {
//            userProfile.setProfileImage(profileImage);
//            userProfileRepository.saveAndFlush(userProfile);
//        }
//    }

    public void deleteUserProfileById(Long id){
        userProfileRepository.deleteById(id);
    }
}
