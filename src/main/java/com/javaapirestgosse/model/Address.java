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

    @jakarta.validation.constraints.NotBlank(message = "La rue est obligatoire")
    @io.swagger.v3.oas.annotations.media.Schema(example = "10 rue de la Paix")
    private String street;
    
    @jakarta.validation.constraints.NotBlank(message = "La ville est obligatoire")
    @io.swagger.v3.oas.annotations.media.Schema(example = "Paris")
    private String city;
    
    @jakarta.validation.constraints.NotBlank(message = "Le code postal est obligatoire")
    @io.swagger.v3.oas.annotations.media.Schema(example = "75001")
    private String postalCode;
    
    @jakarta.validation.constraints.NotBlank(message = "Le pays est obligatoire")
    @io.swagger.v3.oas.annotations.media.Schema(example = "France")
    private String country;

    @OneToOne(mappedBy = "address")
    @JsonIgnore
    private Account account;
}
