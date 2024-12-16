package com.sdtvnews.sdtvnews.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class NewsArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int cateId;

    @Lob
    private String content;  // Store the article text

    private LocalDateTime createDate;

    private Long createBy;

    private String status;

    private String statusMarquee;// 0 = off ; 1=show

    private Long updateStatusBy;

    private LocalDateTime updateStatusDate;

    private LocalDateTime updateDate;

    private Long updateBy;

}

