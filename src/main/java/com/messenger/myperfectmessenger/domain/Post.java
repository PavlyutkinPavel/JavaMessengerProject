package com.messenger.myperfectmessenger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
@Entity(name = "posts")
@Data
public class Post {
    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "posts_id_seq", allocationSize = 1)//для нерандомных id а по sequence
    @GeneratedValue(generator = "mySeqGen")
    private Long id;

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

    @Column(name = "post_likes")
    private Long likes;

    @Column(name = "post_dislikes")
    private Long dislikes;

    @Column(name = "comment_number")
    private Long comments;

    @Column(name = "user_id")
    private Long userId;

}