package com.papelariafrasato.api.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String order_id;

    private LocalDateTime created_at;
    private String status; //Open / Finished / Confirmed / Coming / Rejected
    private double total;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> order_item;

    @PrePersist
    public void onCreate(){
        this.created_at = LocalDateTime.now();
    }

    public Order() {
    }

    public Order(String order_id, LocalDateTime created_at, String status, double total, User user, List<OrderItem> order_item) {
        this.order_id = order_id;
        this.created_at = created_at;
        this.status = status;
        this.total = total;
        this.user = user;
        this.order_item = order_item;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrder_item() {
        return order_item;
    }

    public void setOrder_item(List<OrderItem> order_item) {
        this.order_item = order_item;
    }
}
