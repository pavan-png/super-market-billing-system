package com.supermarket.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product")  // Explicitly matches your table name
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")  // Matches DESC (case-insensitive in Oracle, but explicit is better)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private double price;

    // Getters and Setters (unchanged)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}