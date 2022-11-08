package com.tukks.mythoughtback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetDTO {
    private String url;
    private String content;
    private String media;
    private String author;
    private String hashtag;
}
