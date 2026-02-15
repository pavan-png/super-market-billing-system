package com.supermarket.controller;

import com.supermarket.entity.Order;
import com.supermarket.entity.OrderItem;
import com.supermarket.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<Order> submitOrder(@RequestBody Order order) {
        if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
            order.setOrderId(UUID.randomUUID().toString());
        }
        order.setDate(new Date());

        double total = 0;
        for (OrderItem item : order.getItems()) {
            if (item.getProduct() != null) {
                item.setSubtotal(item.getQuantity() * item.getProduct().getPrice());
                total += item.getSubtotal();
            }
            item.setOrder(order);
        }
        order.setTotal(total);

        // Set cashier from logged-in user
        String cashier = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setCashierUsername(cashier);

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}