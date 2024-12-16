package com.sdtvnews.sdtvnews.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    private Long id;

    private String name;

    private String description;

    private String status;

    private String createBy;

    private String updateBy;

    private String deleteBy;

    private int indexShow;

    private String direction;
}
