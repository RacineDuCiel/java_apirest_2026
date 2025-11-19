package com.javaapirestgosse.service;

import com.javaapirestgosse.dto.NoticeRequest;
import com.javaapirestgosse.dto.NoticeResponse;
import com.javaapirestgosse.model.Account;
import com.javaapirestgosse.model.Notice;
import com.javaapirestgosse.model.Orders;
import com.javaapirestgosse.model.Product;
import com.javaapirestgosse.repository.AccountRepository;
import com.javaapirestgosse.repository.NoticeRepository;
import com.javaapirestgosse.repository.OrdersRepository;
import com.javaapirestgosse.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    public NoticeService(NoticeRepository noticeRepository, OrdersRepository ordersRepository, ProductRepository productRepository, AccountRepository accountRepository) {
        this.noticeRepository = noticeRepository;
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public NoticeResponse createNotice(NoticeRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Impossible de publier un avis : l'utilisateur connecté est introuvable."));

        Orders order = ordersRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Impossible de publier un avis : la commande spécifiée (ID: " + request.getOrderId() + ") n'existe pas."));

        // Vérifier que la commande appartient à l'utilisateur
        if (!order.getAccount().getAccountId().equals(account.getAccountId())) {
            throw new IllegalArgumentException("Action refusée : vous ne pouvez pas laisser d'avis sur une commande qui ne vous appartient pas.");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Impossible de publier un avis : le produit spécifié (ID: " + request.getProductId() + ") n'existe pas."));

        // Vérifier que le produit est bien dans la commande
        boolean productInOrder = order.getOrderDetails().stream()
                .anyMatch(detail -> detail.getProduct().getProductId().equals(product.getProductId()));

        if (!productInOrder) {
            throw new IllegalArgumentException("Action refusée : vous ne pouvez pas évaluer le produit '" + product.getName() + "' car il ne fait pas partie de la commande n°" + order.getOrdersId() + ".");
        }

        Notice notice = new Notice();
        notice.setAccount(account);
        notice.setOrders(order);
        notice.setProduct(product);
        notice.setComment(request.getComment());
        notice.setRating(request.getRating());
        notice.setCreatedAt(LocalDateTime.now());

        Notice savedNotice = noticeRepository.save(notice);
        return mapToResponse(savedNotice);
    }

    public List<NoticeResponse> getProductNotices(Long productId) {
        return noticeRepository.findByProduct_ProductId(productId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private NoticeResponse mapToResponse(Notice notice) {
        NoticeResponse response = new NoticeResponse();
        response.setNoticeId(notice.getNoticeId());
        response.setComment(notice.getComment());
        response.setRating(notice.getRating());
        response.setCreatedAt(notice.getCreatedAt());
        response.setUsername(notice.getAccount().getUsername());
        response.setProductName(notice.getProduct().getName());
        return response;
    }
}
