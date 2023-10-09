package com.messenger.myperfectmessenger.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity(name = "l_users_friends_lists")
@Data
public class UserFriendRelation {

    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "l_users_friends_lists_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "mySeqGen")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_list_id", referencedColumnName = "id")
    private FriendsList friendsList;

    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private User friend;
}