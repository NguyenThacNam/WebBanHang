package com.bkap.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.dto.CartItemDTO;
import com.bkap.entity.Order;
import com.bkap.entity.OrderDetail;
import com.bkap.entity.Product;
import com.bkap.entity.User;
import com.bkap.gateway.PaymentGateway;
import com.bkap.respository.OrderRepository;
import com.bkap.respository.ProductRepository;

@Service
public class OrderService {

	@Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentGateway paymentGateway;
    
    
    

    public Order createOrder(User user, List<CartItemDTO> cartItems, String address, String note, String paymentMethod) {
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setNote(note);
        order.setPaymentMethod(paymentMethod);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(product.getPrice());
            orderDetails.add(detail);
        }

        order.setOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    public String initiatePayment(Long orderId, String ipAddress) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (!"PENDING".equals(order.getPaymentStatus())) {
            throw new IllegalStateException("Order is not in PENDING status");
        }
        String paymentUrl = paymentGateway.createPaymentRequest(order, ipAddress);
        order.setTransactionId(order.getId().toString()); // Lưu transactionId (có thể tùy chỉnh)
        orderRepository.save(order);
        return paymentUrl;
    }

    public void handlePaymentCallback(Map<String, String> callbackParams) {
        String transactionId = callbackParams.get("vnp_TxnRef");
        Order order = orderRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        boolean isValid = paymentGateway.verifyPaymentCallback(callbackParams);
        if (isValid) {
            order.setPaymentStatus("PAID");
            order.setStatus("CONFIRMED"); // Cập nhật trạng thái đơn hàng
        } else {
            order.setPaymentStatus("FAILED");
        }
        orderRepository.save(order);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

        order.setStatus(newStatus);

        // Cập nhật trạng thái thanh toán nếu đơn hàng đã giao và là tiền mặt
        if ("SHIPPED".equalsIgnoreCase(newStatus) && "COD".equalsIgnoreCase(order.getPaymentMethod())) {
            order.setPaymentStatus("PAID");
        }

        orderRepository.save(order);
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng để xóa");
        }
        orderRepository.deleteById(orderId);
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + id));
    }
    
    public BigDecimal calculateTotalAmount(Order order) {
        if (order == null || order.getOrderDetails() == null) {
            return BigDecimal.ZERO;
        }

        return order.getOrderDetails().stream()
            .map(item -> {
                BigDecimal price = BigDecimal.valueOf(item.getPrice()); // double -> BigDecimal
                int quantity = item.getQuantity(); // int, luôn có giá trị
                return price.multiply(BigDecimal.valueOf(quantity));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    public Order getOrderByIdAndUserId(Long orderId, Long userId) throws IllegalAccessException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.getUser() == null || order.getUser().getId() != userId) {
            throw new IllegalAccessException("Bạn không có quyền xem đơn hàng này");
        }
        return order;
    }


}

