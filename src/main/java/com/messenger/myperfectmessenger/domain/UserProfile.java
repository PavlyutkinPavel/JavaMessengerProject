package com.messenger.myperfectmessenger.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.Date;

@Entity(name = "user_profiles")
@Data
public class UserProfile {
    @Schema(description = "Это уникальный идентификатор пользователя")
    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "user_profile_id_seq", allocationSize = 1)//для нерандомных id а по sequence
    @GeneratedValue(generator = "mySeqGen")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "description")
    private String profileDescription;

    @Column(name = "birth_date")
    private Date dateOfBirth;

    @Lob
    @Column(name = "image", columnDefinition = "clob")
    private Long profileImage; // Поле для хранения изображения профиля в виде байтов
}
