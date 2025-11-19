package com.javaapirestgosse.dto;

import lombok.Data;

@Data
public class NoticeRequest {
    @io.swagger.v3.oas.annotations.media.Schema(example = "1")
    private Long orderId;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "1")
    private Long productId;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "Excellent produit !")
    private String comment;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "5")
    private Integer rating;
}
