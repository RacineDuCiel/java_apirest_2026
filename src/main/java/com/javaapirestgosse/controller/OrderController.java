package com.javaapirestgosse.controller;

import com.javaapirestgosse.dto.CreateOrderRequest;
import com.javaapirestgosse.dto.OrderSummaryResponse;
import com.javaapirestgosse.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Commandes", description = "Consultation et création de commandes")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @Operation(summary = "Historique personnel des commandes",
        description = "Accessible aux utilisateurs authentifiés (ROLE_USER / ROLE_ADMIN). Permet de filtrer par statut avec pagination et tri.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<Page<OrderSummaryResponse>> getMyOrders(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
            @RequestParam(value = "direction", defaultValue = "DESC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<OrderSummaryResponse> response = orderService.getMyOrders(status, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @Operation(summary = "Créer une commande",
        description = "Accessible aux utilisateurs authentifiés (ROLE_USER / ROLE_ADMIN). Décrémente les stocks et retourne un résumé de la commande.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<OrderSummaryResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderSummaryResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
