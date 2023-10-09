package com.messenger.myperfectmessenger.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;

@Component
@Schema(description = "Описание пользователя")
@Data
@Entity(name = "users")//указываем hibernate в какую табл идти
@EqualsAndHashCode(exclude = {"chats", "friends"})
@ToString(exclude = {"chats", "friends"})
public class User {

    @Schema(description = "Это уникальный идентификатор пользователя")
    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "users_id_seq", allocationSize = 1)//для нерандомных id а по sequence
    @GeneratedValue(generator = "mySeqGen")
    //@TableGenerator()
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created")
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Collection<Chat> chats;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Collection<FriendsList> friends;

}
