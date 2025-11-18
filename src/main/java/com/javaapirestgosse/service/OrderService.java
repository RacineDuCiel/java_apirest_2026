package com.javaapirestgosse.service;

import com.javaapirestgosse.dto.CreateOrderRequest;
import com.javaapirestgosse.dto.OrderItemRequest;
import com.javaapirestgosse.dto.OrderItemSummary;
import com.javaapirestgosse.dto.OrderSummaryResponse;
import com.javaapirestgosse.model.Account;
import com.javaapirestgosse.model.OrderDetails;
import com.javaapirestgosse.model.Orders;
import com.javaapirestgosse.model.Product;
import com.javaapirestgosse.repository.AccountRepository;
import com.javaapirestgosse.repository.OrdersRepository;
import com.javaapirestgosse.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    public OrderService(OrdersRepository ordersRepository,
                        ProductRepository productRepository,
                        AccountRepository accountRepository) {
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getMyOrders(String status, Pageable pageable) {
        Account account = getCurrentAccount();
        Page<Orders> ordersPage;

        if (status != null && !status.isBlank()) {
            ordersPage = ordersRepository.findByAccountAndStatusIgnoreCase(account, status, pageable);
        } else {
            ordersPage = ordersRepository.findByAccount(account, pageable);
        }

        return ordersPage.map(this::mapToSummary);
    }

    @Transactional
    public OrderSummaryResponse createOrder(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir au moins un produit");
        }

        Account account = getCurrentAccount();
        Orders orders = new Orders();
        orders.setAccount(account);
        orders.setOrderDate(LocalDateTime.now());
        orders.setStatus("CREATED");

        List<OrderDetails> details = new ArrayList<>();

        for (OrderItemRequest item : request.getItems()) {
            OrderDetails detail = buildOrderDetail(item, orders);
            details.add(detail);
        }

        orders.setOrderDetails(details);

        Orders savedOrder = ordersRepository.save(orders);
        return mapToSummary(savedOrder);
    }

    private OrderDetails buildOrderDetail(OrderItemRequest item, Orders orders) {
        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable: " + item.getProductId()));

        int requestedQuantity = item.getQuantity();
        if (requestedQuantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }

        int available = product.getAvailableQuantity() != null ? product.getAvailableQuantity() : 0;
        if (available < requestedQuantity) {
            throw new IllegalArgumentException("Stock insuffisant pour le produit: " + product.getName());
        }

        product.setAvailableQuantity(available - requestedQuantity);
        productRepository.save(product);

        OrderDetails detail = new OrderDetails();
        detail.setOrders(orders);
        detail.setProduct(product);
        detail.setQuantity(requestedQuantity);
        detail.setUnitPrice(product.getPrice());
        return detail;
    }

    private OrderSummaryResponse mapToSummary(Orders orders) {
        List<OrderItemSummary> items = orders.getOrderDetails() == null ? List.of() :
                orders.getOrderDetails().stream()
                        .map(detail -> new OrderItemSummary(
                                detail.getProduct().getProductId(),
                                detail.getProduct().getName(),
                                detail.getQuantity(),
                                detail.getUnitPrice(),
                                detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()))
                        ))
                        .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(OrderItemSummary::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderSummaryResponse.builder()
                .orderId(orders.getOrdersId())
                .placedAt(orders.getOrderDate())
                .status(orders.getStatus())
                .totalAmount(total)
                .itemCount(items.size())
                .items(items)
                .build();
    }

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }

        return accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));
    }
}
