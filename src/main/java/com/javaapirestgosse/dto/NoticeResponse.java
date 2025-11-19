package com.javaapirestgosse.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoticeResponse {
    private Long noticeId;
    private String comment;
    private Integer rating;
    private LocalDateTime createdAt;
    private String username;
    private String productName;
}
