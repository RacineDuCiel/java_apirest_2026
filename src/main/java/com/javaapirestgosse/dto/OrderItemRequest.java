package com.javaapirestgosse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {

    @NotNull(message = "L'identifiant du produit est obligatoire")
    @io.swagger.v3.oas.annotations.media.Schema(example = "1")
    private Long productId;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    @io.swagger.v3.oas.annotations.media.Schema(example = "2")
    private Integer quantity;
}
