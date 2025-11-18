package com.javaapirestgosse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "product")
public class Product {

    private static final int LOW_STOCK_THRESHOLD = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer availableQuantity;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Notice> notices;

    @Transient
    @JsonProperty("lowStock")
    public boolean isLowStock() {
        return availableQuantity != null && availableQuantity <= LOW_STOCK_THRESHOLD;
    }

    @PrePersist
    @PreUpdate
    private void ensureAvailability() {
        if (availableQuantity == null) {
            availableQuantity = stockQuantity != null ? stockQuantity : 0;
        }
        if (availableQuantity < 0) {
            availableQuantity = 0;
        }
    }
}
