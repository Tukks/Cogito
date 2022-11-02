package com.tukks.mythoughtback.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Tweet")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TweetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url")
    private String url;
    @Column(name = "content")
    private String content;
    @Column(name = "media")
    private String media;
    @Column(name = "author")
    private String author;
    @Column(name = "hashtag")
    private String hashtag;

    public TweetEntity(String url, String content, String media, String author, String hashtag) {
        this.url = url;
        this.content = content;
        this.media = media;
        this.author = author;
        this.hashtag = hashtag;
    }
}
