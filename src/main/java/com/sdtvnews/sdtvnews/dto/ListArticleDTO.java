package com.sdtvnews.sdtvnews.dto;

import java.time.LocalDateTime;


public interface ListArticleDTO {

    Long getId();
    String getTitle();
    String getContent();
    LocalDateTime getCreateDate();
    String getName();
    String getFullName();
    String getStatus();

}
