package com.example.demo.repository;

import com.example.demo.model.Member;
import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    List<ProductOrder> findByMember(Member member);
    List<ProductOrder> findByProduct(Product product);
}
