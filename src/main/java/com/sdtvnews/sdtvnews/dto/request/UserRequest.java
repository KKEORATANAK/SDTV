package com.sdtvnews.sdtvnews.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserRequest {

    private Long id;

    private String firstName;
    private String lastName;

    private String userName;
    private String passWord;

    private String status;

    private int roleId;

    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime deleteDate;
    private String deleteBy;
    private LocalDateTime updateDate;
    private String updateBy;

}
