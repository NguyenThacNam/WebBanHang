package com.bkap.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.bkap.dto.CartItemDTO;
import com.bkap.entity.Order;
import com.bkap.entity.Product;
import com.bkap.entity.User;
import com.bkap.service.CartService;
import com.bkap.service.OrderService;
import com.bkap.service.ProductService;
import com.bkap.service.UserService;
import com.cart.CartItem;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
  
	 @Autowired
	    private CartService cartService;

	    @Autowired
	    private ProductService productService; // Giả định bạn có ProductService để lấy thông tin sản phẩm
	    
	    
	    @Autowired
	    private UserService  userService;
	    
	    
	    @Autowired
	    private OrderService  orderService;

	    @GetMapping("/checkout")
	    public String showCheckoutPage(HttpSession session, Model model) {
	        List<CartItem> cartItems = cartService.getCart(session);
	        
	        // Chuyển đổi CartItem sang CartItemDTO và thêm thông tin sản phẩm
	        List<CartItemDTO> cartItemDTOs = cartItems.stream().map(item -> {
	            CartItemDTO dto = new CartItemDTO();
	            dto.setProductId(item.getProductId());
	            dto.setQuantity(item.getQuantity());
	            return dto;
	        }).collect(Collectors.toList());

	        // Thêm thông tin sản phẩm (tên, giá) để hiển thị
	        List<CartItemDisplay> cartItemDisplays = cartItems.stream().map(item -> {
	            Product product = productService.getProductById(item.getProductId()); 
	            return new CartItemDisplay(
	                    product.getId(),
	                    product.getName(),
	                    item.getQuantity(),
	                    product.getPrice()
	            );
	        }).collect(Collectors.toList());

	        model.addAttribute("cartItems", cartItemDisplays);
	        model.addAttribute("cartItemsJson", cartItemDTOs); // Dùng cho JavaScript
	        model.addAttribute("total", cartService.getTotal(session));
	        return "site/checkout";
	    }

	    @GetMapping("/payment-result")
	    public String showPaymentResult(@RequestParam("status") String status, Model model, HttpSession session) {
	        if ("00".equals(status)) {
	            cartService.clearCart(session); // Xóa giỏ hàng sau khi thanh toán thành công
	        }
	        model.addAttribute("status", status);
	        return "site/payment-result";
	    }
	    @GetMapping("/my-orders")
	    public String showMyOrders(Principal principal, Model model) {
	        String username = principal.getName();
	        User user = userService.findByUsername(username);
	        if (user == null) {
	            model.addAttribute("error", "Vui lòng đăng nhập để xem đơn hàng.");
	            return "site/my-orders";
	        }

	        List<Order> orders = orderService.getOrdersByUserId(user.getId());

	        // Tính tổng tiền cho từng đơn
	        Map<Long, BigDecimal> orderTotals = new HashMap<>();
	        for (Order order : orders) {
	            BigDecimal total = orderService.calculateTotalAmount(order);
	            orderTotals.put(order.getId(), total);
	        }

	        model.addAttribute("orders", orders);
	        model.addAttribute("orderTotals", orderTotals);

	        return "site/my-orders";
	    }


	    @GetMapping("/my-orders/{orderId}")
	    public String showOrderDetails(@PathVariable Long orderId, Principal principal, Model model) {
	        String username = principal.getName();
	        User user = userService.findByUsername(username);
	        
	        if (user == null) {
	            model.addAttribute("error", "Vui lòng đăng nhập để xem đơn hàng.");
	            return "site/order-details";
	        }

	        try {
	            Order order = orderService.getOrderByIdAndUserId(orderId, user.getId());
	            model.addAttribute("order", order);

	            // Tính tổng tiền
	            BigDecimal totalAmount = order.getOrderDetails().stream()
	                .map(detail -> BigDecimal.valueOf(detail.getPrice())
	                    .multiply(BigDecimal.valueOf(detail.getQuantity())))
	                .reduce(BigDecimal.ZERO, BigDecimal::add);

	            model.addAttribute("totalAmount", totalAmount);

	            return "site/order-details";
	        } catch (IllegalAccessException e) {
	            model.addAttribute("error", "Bạn không có quyền xem đơn hàng này.");
	            return "site/order-details";
	        } catch (IllegalArgumentException e) {
	            model.addAttribute("error", "Đơn hàng không tồn tại.");
	            return "site/order-details";
	        }
	    }

	    
	}

	// Class tạm thời để hiển thị thông tin giỏ hàng trong view
	class CartItemDisplay {
	    private Long productId;
	    private String productName;
	    private int quantity;
	    private double price;

	    public CartItemDisplay(Long productId, String productName, int quantity, double price) {
	        this.productId = productId;
	        this.productName = productName;
	        this.quantity = quantity;
	        this.price = price;
	    }

	    // Getters
	    public Long getProductId() { return productId; }
	    public String getProductName() { return productName; }
	    public int getQuantity() { return quantity; }
	    public double getPrice() { return price; }
	}
