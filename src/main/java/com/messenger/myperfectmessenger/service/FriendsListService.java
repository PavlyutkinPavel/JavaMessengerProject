package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.FriendRequest;
import com.messenger.myperfectmessenger.domain.FriendsList;
import com.messenger.myperfectmessenger.domain.Role;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.exception.FriendNotFoundException;
import com.messenger.myperfectmessenger.exception.UserNotFoundException;
import com.messenger.myperfectmessenger.repository.FriendRequestRepository;
import com.messenger.myperfectmessenger.repository.FriendsListRepository;
import com.messenger.myperfectmessenger.security.domain.SecurityCredentials;
import com.messenger.myperfectmessenger.security.repository.SecurityCredentialsRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FriendsListService {
    private final FriendsListRepository friendsListRepository;
    private final FriendRequestRepository friendRequestRepository;

    private final SecurityCredentialsRepository securityCredentialsRepository;

    public FriendsListService(FriendsListRepository friendsListRepository, FriendRequestRepository friendRequestRepository, SecurityCredentialsRepository securityCredentialsRepository) {
        this.friendsListRepository = friendsListRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.securityCredentialsRepository = securityCredentialsRepository;
    }

    public List<FriendsList> getFriendsLists() {
        return friendsListRepository.findAll();
    }
    public FriendsList getFriendsList(Long id, Principal principal) {
        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(principal.getName()).orElseThrow(UserNotFoundException::new);
        FriendsList friendsList =  friendsListRepository.findById(id).orElseThrow(FriendNotFoundException::new);
        Role role = credentials.getUserRole();
        if((friendsList.getFriendName() == principal.getName()) || (role.toString() == "ADMIN")){
            return friendsList;
        }else{
            return null;
        }
    }
    public List<FriendRequest> getFriendRequests(String username) {
        return friendRequestRepository.findAllByUsername(username);
    }
    public void createFriendsList(FriendsList friendsList) {
        friendsList.setFriendSince(LocalDate.now());
        friendsListRepository.save(friendsList);
    }

    public void updateFriendsList(FriendsList friendsList) {
        friendsListRepository.updateFriendsList(friendsList);
    }

    public void deleteFriendsList(FriendsList friendsList){
        friendsListRepository.deleteFriendsList(friendsList);
    }

    public List<FriendsList> findFriendsByIsClose(Boolean isClose) {
        return friendsListRepository.findByIsClose(isClose);
    }

    public List<FriendsList> findFriendsByFriendSince(Date friendSince, Principal principal) {
        SecurityCredentials credentials = securityCredentialsRepository.findUserIdByLogin(principal.getName()).orElseThrow(UserNotFoundException::new);
        List<FriendsList> friendsLists = friendsListRepository.findByFriendSinceGreaterThanEqual(friendSince);
        Role role = credentials.getUserRole();
        if((role.toString() == "ADMIN")){
            return friendsLists;
        }else{
            return null;
        }

    }

    public void createFriendRequest(String friendName, FriendRequest friendRequest, Principal principal) {
        friendRequest.setFriend(friendName);
        friendRequest.setSender(principal.getName());
        friendRequest.setSendTime(LocalDateTime.now());
        friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(Boolean accepted, Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElseThrow(FriendNotFoundException::new);
        if(accepted){
            friendRequest.setIsAccepted(true);
            friendRequestRepository.saveAndFlush(friendRequest);
        }else{
            friendRequestRepository.delete(friendRequest);
        }
    }
}
