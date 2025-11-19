package com.javaapirestgosse.controller;

import com.javaapirestgosse.dto.NoticeRequest;
import com.javaapirestgosse.dto.NoticeResponse;
import com.javaapirestgosse.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@Tag(name = "Avis", description = "Gestion des avis sur les produits commandés")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Créer un avis",
            description = "Accessible aux utilisateurs authentifiés. L'utilisateur doit avoir commandé le produit.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<NoticeResponse> createNotice(@RequestBody NoticeRequest request) {
        NoticeResponse response = noticeService.createNotice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Lister les avis d'un produit",
            description = "Route publique. Retourne tous les avis liés à un produit.")
    public ResponseEntity<List<NoticeResponse>> getProductNotices(@PathVariable Long productId) {
        List<NoticeResponse> notices = noticeService.getProductNotices(productId);
        return ResponseEntity.ok(notices);
    }
}
