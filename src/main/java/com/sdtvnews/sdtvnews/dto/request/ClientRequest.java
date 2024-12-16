package com.sdtvnews.sdtvnews.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequest {

    private Long id;

    private String name;

    private String phoneNum;

    private String description;

    private String status;

}
