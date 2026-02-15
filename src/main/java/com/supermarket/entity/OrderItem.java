package com.supermarket.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @JsonIgnore  // Add this to prevent circular reference in JSON serialization
    @ManyToOne
    @JoinColumn(
        name = "ORDER_ORDER_ID",
        referencedColumnName = "ORDER_ID"
    )
    private Order order;

    @ManyToOne
    @JoinColumn(
        name = "PRODUCT_ID",
        referencedColumnName = "ID"
    )
    private Product product;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "SUBTOTAL")
    private double subtotal;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}