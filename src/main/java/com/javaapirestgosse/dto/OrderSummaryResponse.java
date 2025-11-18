package com.javaapirestgosse.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderSummaryResponse {
    private Long orderId;
    private LocalDateTime placedAt;
    private String status;
    private BigDecimal totalAmount;
    private Integer itemCount;
    private List<OrderItemSummary> items;
}
