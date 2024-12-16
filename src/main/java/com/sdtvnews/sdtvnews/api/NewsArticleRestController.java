package com.sdtvnews.sdtvnews.api;


import com.sdtvnews.sdtvnews.config.TelegramService;
import com.sdtvnews.sdtvnews.config.exception.ResponseDTO;
import com.sdtvnews.sdtvnews.dto.ListArticleDTO;
import com.sdtvnews.sdtvnews.dto.request.NewsArticleRequest;
import com.sdtvnews.sdtvnews.dto.request.TelegramRequest;
import com.sdtvnews.sdtvnews.entity.NewsArticle;
import com.sdtvnews.sdtvnews.services.NewsArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsArticleRestController {

    private final NewsArticleService newsArticleService;

    @Autowired
    private TelegramService telegramService;

    @PostMapping("/send")
    public String sendToTelegram(@RequestBody TelegramRequest telegramRequest) {
        return telegramService.sendMessage(telegramRequest.getTitle() , telegramRequest.getUrl());
    }


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createArticle(@RequestParam("title") String title,
                                           @RequestParam("content") String content,
                                           @RequestParam("images") MultipartFile[] images) {
        try {
            NewsArticleRequest request = new NewsArticleRequest();
            request.setTitle(title);
            request.setContent(content);
            NewsArticle createdArticle = newsArticleService.createArticle(request);
            return ResponseEntity.ok(new ResponseDTO<>("success", "News article created successfully", createdArticle));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO<>("error", "An error occurred", null));
        }
    }


    // Get all news articles
    @GetMapping("/all")
    public ResponseEntity<?> getAllArticles() {
        List<ListArticleDTO> articles = newsArticleService.getAllArticles();
        return ResponseEntity.ok(new ResponseDTO<>("success", "List of news articles", articles));
    }

    // Get news article by ID
    @GetMapping("/find/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        try {
            NewsArticle article = newsArticleService.getArticleById(id);
            return ResponseEntity.ok(new ResponseDTO<>("success", "Article found", article));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ResponseDTO<>("error", e.getMessage(), null));
        }
    }

    // Delete an article (soft delete by updating status)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        newsArticleService.deleteArticle(id);
        return ResponseEntity.ok(new ResponseDTO<>("success", "Article deleted successfully", null));
    }
}
