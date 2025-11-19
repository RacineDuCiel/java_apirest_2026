package com.javaapirestgosse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersId;

    private LocalDateTime orderDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notice> notices;
}
