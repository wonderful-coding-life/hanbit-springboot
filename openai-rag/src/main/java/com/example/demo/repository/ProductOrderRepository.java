package com.example.demo.repository;

import com.example.demo.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    Optional<ProductOrder> findByOrderNumber(String orderNumber);
}
