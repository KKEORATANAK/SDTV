package com.sdtvnews.sdtvnews.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class News {
    private String id;
    private String title;
    private String firstImage;
    private LocalDateTime createDate;
}
