package com.sdtvnews.sdtvnews.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewsArticleRequest {
    private Long id;
    private String title;
    private String content;
    private int cateId;
    private Long userId;
    private String statusMarquee;

}
