package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.FriendRequest;
import com.messenger.myperfectmessenger.domain.FriendsList;
import com.messenger.myperfectmessenger.exception.FriendNotFoundException;
import com.messenger.myperfectmessenger.repository.FriendRequestRepository;
import com.messenger.myperfectmessenger.security.service.SecurityService;
import com.messenger.myperfectmessenger.service.FriendsListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Tag(name = "Friend Controller", description = "Makes all operations with friendlists and friendrequests")
@RestController //для REST архитектуры
@RequestMapping("/friend")
public class FriendsListController {
    private final FriendsListService friendsListService;
    private final SecurityService securityService;

    private final FriendRequestRepository friendRequestRepository;

    public FriendsListController(FriendsListService friendsListService, SecurityService securityService, FriendRequestRepository friendRequestRepository) {
        this.friendsListService = friendsListService;
        this.securityService = securityService;
        this.friendRequestRepository = friendRequestRepository;
    }

    @Operation(summary = " get all friend lists(for admins)")
    @GetMapping
    public ResponseEntity<List<FriendsList>> getFriendsLists(Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            List<FriendsList> friendsLists = friendsListService.getFriendsLists();
            if (friendsLists.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(friendsLists, HttpStatus.OK);
            }
        } else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = " get all close friend(for admins)")
    @GetMapping("/close")
    public ResponseEntity<List<FriendsList>> findFriendsByIsClose(@RequestParam Boolean isClose, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())) {
            List<FriendsList> friends = friendsListService.findFriendsByIsClose(isClose);
            return new ResponseEntity<>(friends, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = " get all friend since some time(for admins and authorized users)")
    @GetMapping("/friends_since")
    public ResponseEntity<List<FriendsList>> findFriendsByFriendSince(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date friendSince, Principal principal) {
        List<FriendsList> friends = friendsListService.findFriendsByFriendSince(friendSince, principal);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @Operation(summary = " get all your friends(for authorized users)")
    @GetMapping("/{id}")
    public ResponseEntity<FriendsList> getFriendsList(@PathVariable Long id, Principal principal) {
        FriendsList friendsList = friendsListService.getFriendsList(id, principal);
        if(friendsList != null){
            return new ResponseEntity<>(friendsList, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = " get all your friend requests (for admins and authorized users)")
    @GetMapping("/friend_requests/{username}")
    public ResponseEntity<List<FriendRequest>> getFriendRequests(@PathVariable String username, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || principal.getName().equals(username)){
            List<FriendRequest> friendRequests = friendsListService.getFriendRequests(username);
            if (friendRequests.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(friendRequests, HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>( HttpStatus.FORBIDDEN);
        }

    }

    @Operation(summary = "create friend lists(for authorized users)")
    @PostMapping
    public ResponseEntity<HttpStatus> createFriendsList(@RequestBody FriendsList friendsList) {
        friendsListService.createFriendsList(friendsList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "create friend request(for authorized users)")
    @PostMapping("/send_request/{friend_name}")
    public ResponseEntity<HttpStatus> createFriendRequest(@PathVariable String friend_name, @RequestBody FriendRequest friendRequest, Principal principal) {
        friendsListService.createFriendRequest(friend_name, friendRequest, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "accept your friend request(for admins and authorized users)")
    @PutMapping("/accept_request/{requestId}")
    public ResponseEntity<HttpStatus> acceptFriendRequest(@RequestParam Boolean accepted, @PathVariable Long requestId, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName()) || friendRequestRepository.findById(requestId).orElseThrow(FriendNotFoundException::new).getFriend().equals(principal.getName())){
            friendsListService.acceptFriendRequest(accepted, requestId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>( HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "update friend list(for authorized users)")
    @PutMapping
    public ResponseEntity<HttpStatus> updateFriendsList(@RequestBody FriendsList friendsList) {
        friendsListService.updateFriendsList(friendsList);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "delete friend list(for admins)")
    @DeleteMapping("/delete_friend")
    public ResponseEntity<HttpStatus> deleteFriendsList(@RequestBody FriendsList friendsList, Principal principal) {
        if(securityService.checkIfAdmin(principal.getName())){
            friendsListService.deleteFriendsList(friendsList);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
