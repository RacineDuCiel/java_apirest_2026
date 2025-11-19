package com.javaapirestgosse.dto;

import lombok.Data;

@Data
public class NoticeRequest {
    private Long orderId;
    private Long productId;
    private String comment;
    private Integer rating;
}
