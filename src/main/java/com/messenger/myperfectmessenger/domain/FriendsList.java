package com.messenger.myperfectmessenger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

import java.util.Date;

@Entity(name = "friends_lists")
@Data
public class FriendsList {
    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "friends_lists_id_seq", allocationSize = 1)//для нерандомных id а по sequence
    @GeneratedValue(generator = "mySeqGen")
    private Long id;

    @Column(name = "close_friend")
    private Boolean isClose;

    @Column(name = "friends_since")
    private Date friendSince;

}