package com.sdtvnews.sdtvnews.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.channel.id}")
    private String channelId;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendMessage(String title, String url) {
        String telegramUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        String message = "*" + title + "*\n" + url;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", channelId);
        requestBody.put("text", message);
        requestBody.put("parse_mode", "Markdown");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            return restTemplate.postForObject(telegramUrl, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send message: " + e.getMessage();
        }
    }
}
