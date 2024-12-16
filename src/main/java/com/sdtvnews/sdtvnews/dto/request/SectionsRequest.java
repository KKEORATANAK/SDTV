package com.sdtvnews.sdtvnews.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SectionsRequest {

    private Long id;

    private String name;

    private String description;

    private String status;

    private String createBy;

    private String updateBy;

    private String deleteBy;

    private String indexShow;
}
