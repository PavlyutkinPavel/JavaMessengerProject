package com.messenger.myperfectmessenger.controller;

import com.messenger.myperfectmessenger.domain.FriendsList;
import com.messenger.myperfectmessenger.service.FriendsListService;
import com.messenger.myperfectmessenger.exception.FriendNotFoundException;
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

import java.util.Date;
import java.util.List;

@RestController //для REST архитектуры
@RequestMapping("/friends_list")
public class FriendsListController {
    private final FriendsListService friendsListService;

    public FriendsListController(FriendsListService friendsListService) {
        this.friendsListService = friendsListService;
    }

    @GetMapping
    public ResponseEntity<List<FriendsList>> getFriendsLists() {
        List<FriendsList> friendsLists = friendsListService.getFriendsLists();
        if (friendsLists.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(friendsLists, HttpStatus.OK);
        }
    }

    @GetMapping("/close")
    public ResponseEntity<List<FriendsList>> findFriendsByIsClose(@RequestParam Boolean isClose) {
        List<FriendsList> friends = friendsListService.findFriendsByIsClose(isClose);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/friends_since")
    public ResponseEntity<List<FriendsList>> findFriendsByFriendSince(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date friendSince) {
        List<FriendsList> friends = friendsListService.findFriendsByFriendSince(friendSince);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FriendsList> getFriendsList(@PathVariable Long id) {
        FriendsList friendsList = friendsListService.getFriendsList(id).orElseThrow(FriendNotFoundException::new);
        return new ResponseEntity<>(friendsList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createFriendsList(@RequestBody FriendsList friendsList) {
        friendsListService.createFriendsList(friendsList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateFriendsList(@RequestBody FriendsList friendsList) {
        friendsListService.updateFriendsList(friendsList);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete_friend")
    public ResponseEntity<HttpStatus> deleteFriendsList(@RequestBody FriendsList friendsList) {
        friendsListService.deleteFriendsList(friendsList);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
