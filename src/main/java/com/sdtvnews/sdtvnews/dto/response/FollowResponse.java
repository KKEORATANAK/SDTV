package com.sdtvnews.sdtvnews.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FollowResponse {

    private Long id;

    private String name;

    private String description;

    private String status;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime deleteDate;

    private String deleteBy;

    private int indexShow;
}
