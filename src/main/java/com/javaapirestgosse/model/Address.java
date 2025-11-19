package com.javaapirestgosse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @io.swagger.v3.oas.annotations.media.Schema(accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY)
    private Long addressId;

    @io.swagger.v3.oas.annotations.media.Schema(example = "10 rue de la Paix")
    private String street;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "Paris")
    private String city;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "75001")
    private String postalCode;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "France")
    private String country;

    @OneToOne(mappedBy = "address")
    @JsonIgnore
    private Account account;
}
