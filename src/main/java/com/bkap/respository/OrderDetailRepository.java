package com.bkap.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bkap.entity.Order;
import com.bkap.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order); // lấy chi tiết theo đơn
}
