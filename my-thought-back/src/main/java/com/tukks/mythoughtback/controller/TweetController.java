package com.tukks.mythoughtback.controller;

import com.tukks.mythoughtback.dto.request.TweetRequest;
import com.tukks.mythoughtback.dto.response.TweetDTO;
import com.tukks.mythoughtback.service.TweetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @GetMapping("/tweets")
    public List<TweetDTO> getAllTweets() {
        return tweetService.getAll();
    }

    @PostMapping("/tweet")
    public ResponseEntity saveTweet(@RequestBody TweetRequest tweetRequest) {
        if(tweetService.addTweet(tweetRequest)){
            return ResponseEntity.ok("Tweet save");
        }
        return ResponseEntity.badRequest().build();


    }
}
