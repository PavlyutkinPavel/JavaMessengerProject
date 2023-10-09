package com.messenger.myperfectmessenger.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity(name = "friend_requests")
public class FriendRequest {
    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "friends_lists_id_seq", allocationSize = 1)//для нерандомных id а по sequence
    @GeneratedValue(generator = "mySeqGen")
    private Long id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "friend")
    private String friend;

    @Column(name = "request_message")
    private String requestMessage;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @Column(name = "is_accepted")
    private Boolean isAccepted;
}
