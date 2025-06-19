package com.bkap.api;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bkap.dto.OrderFormDTO;
import com.bkap.entity.Order;
import com.bkap.entity.User;
import com.bkap.gateway.PaymentGateway;
import com.bkap.service.OrderService;
import com.bkap.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/order")
public class OrderAPI {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrderFormDTO form, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng.");
        }

        Order order = orderService.createOrder(
                user,
                form.getCartItems(),
                form.getAddress(),
                form.getNote(),
                form.getPaymentMethod()
        );

        return ResponseEntity.ok(Map.of("message", "Đặt hàng thành công", "orderId", order.getId()));
    }

    @PostMapping("/initiate-payment")
    public ResponseEntity<?> initiatePayment(@RequestBody Map<String, Long> request, 
                                           @RequestHeader(value = "X-Forwarded-For", defaultValue = "127.0.0.1") String ipAddress) {
        Long orderId = request.get("orderId");
        try {
            String paymentUrl = orderService.initiatePayment(orderId, ipAddress);
            return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không thể khởi tạo thanh toán: " + e.getMessage()));
        }
    }

    @GetMapping("/payment-callback")
    public void handlePaymentCallback(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        try {
            orderService.handlePaymentCallback(params);
            response.sendRedirect("/payment-result?status=" + params.get("vnp_ResponseCode"));
        } catch (Exception e) {
            response.sendRedirect("/payment-result?status=fail");
        }
    }
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng.");
        }
        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/my-orders/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng.");
        }
        try {
            Order order = orderService.getOrderByIdAndUserId(orderId, user.getId());
            return ResponseEntity.ok(order);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).body("Bạn không có quyền xem đơn hàng này.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Đơn hàng không tồn tại.");
        }
    }
}