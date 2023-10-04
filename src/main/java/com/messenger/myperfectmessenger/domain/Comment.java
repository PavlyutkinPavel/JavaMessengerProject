package com.messenger.myperfectmessenger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity(name = "comments")
@Data
public class Comment {
    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "comments_id_seq", allocationSize = 1)//для нерандомных id а по sequence
    @GeneratedValue(generator = "mySeqGen")
    private Long id;

    @Column(name = "comment_content")
    private String content;

    @Column(name = "comment_likes")
    private Long likes;

    @Column(name = "comment_dislikes")
    private Long dislikes;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

}