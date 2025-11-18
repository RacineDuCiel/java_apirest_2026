package com.javaapirestgosse.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotEmpty(message = "Une commande doit contenir au moins un produit")
    @Valid
    private List<OrderItemRequest> items;
}
