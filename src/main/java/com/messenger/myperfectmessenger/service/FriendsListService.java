package com.messenger.myperfectmessenger.service;

import com.messenger.myperfectmessenger.domain.FriendsList;
import com.messenger.myperfectmessenger.domain.User;
import com.messenger.myperfectmessenger.repository.FriendsListRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Service
public class FriendsListService {
    private final FriendsListRepository friendsListRepository;

    public FriendsListService(FriendsListRepository friendsListRepository) {
        this.friendsListRepository = friendsListRepository;
    }

    public List<FriendsList> getFriendsLists() {
        return friendsListRepository.findAll();
    }
    public Optional<FriendsList> getFriendsList(Long id) {
        //проверка на логин
        return friendsListRepository.findById(id);
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

    public List<FriendsList> findFriendsByFriendSince(Date friendSince) {
        return friendsListRepository.findByFriendSinceGreaterThanEqual(friendSince);
    }
}
