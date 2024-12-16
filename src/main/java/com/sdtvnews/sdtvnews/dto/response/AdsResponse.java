package com.sdtvnews.sdtvnews.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdsResponse {
    private Long id;

    private String url;

    private String image;

    private String local;

    private String status;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime deleteDate;

    private String deleteBy;

    private LocalDateTime updateDate;

    private String updateBy;

}
