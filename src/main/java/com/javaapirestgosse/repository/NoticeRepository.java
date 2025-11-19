package com.javaapirestgosse.repository;

import com.javaapirestgosse.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByProduct_ProductId(Long productId);
    List<Notice> findByAccount_AccountId(Long accountId);
}
