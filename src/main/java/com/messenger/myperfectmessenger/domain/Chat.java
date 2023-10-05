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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@Entity(name = "chats")
@Data
@EqualsAndHashCode(exclude = {"users", "messages"})
@ToString(exclude = {"users", "messages"})
public class Chat {
    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "chats_id_seq", allocationSize = 1)//для нерандомных id а по sequence
    @GeneratedValue(generator = "mySeqGen")
    private Long id;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "chat_description")
    private String description;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "l_users_chats", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private Collection<User> users;

    @OneToMany(mappedBy = "chats", fetch = FetchType.EAGER)//название связанного поля, EAGER - все, LAZY - никакие
    private Collection<Message> messages;
}