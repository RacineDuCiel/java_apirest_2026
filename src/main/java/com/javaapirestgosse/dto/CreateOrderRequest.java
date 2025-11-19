package com.javaapirestgosse.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotEmpty(message = "Une commande doit contenir au moins un produit")
    @Valid
    @io.swagger.v3.oas.annotations.media.Schema(description = "Liste des articles commandés")
    private List<OrderItemRequest> items;
}
