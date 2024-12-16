package com.sdtvnews.sdtvnews.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private Long id;

    private String firstName;
    private String lastName;

    private String userName;
    private String passWord;

    private String status;

    private int roleId;

    private String roleName;

    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime deleteDate;
    private String deleteBy;
    private LocalDateTime updateDate;
    private String updateBy;
}
