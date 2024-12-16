package com.sdtvnews.sdtvnews.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ClientResponse {

    private Long id;

    private String name;

    private String phoneNum;

    private String description;

    private String status;

    private LocalDateTime createDate;


}
