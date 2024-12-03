package com.appgate.customer.repository;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private boolean isActive;

    @Column(name = "phishing_detection")
    private boolean isPhishingDetection;

}