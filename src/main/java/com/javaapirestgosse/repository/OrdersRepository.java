package com.javaapirestgosse.repository;

import com.javaapirestgosse.model.Account;
import com.javaapirestgosse.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findByAccount(Account account, Pageable pageable);
    Page<Orders> findByAccountAndStatusIgnoreCase(Account account, String status, Pageable pageable);
}
