package com.sdtvnews.sdtvnews.dto;


import java.time.LocalDateTime;

public interface ListUserDTO {
    Long getId();
    String getLastName();
    String getFirstName();
    String getUserName();
    String getPassWord();
    String getName();
    LocalDateTime getCreateDate();
    String getStatus();
}
