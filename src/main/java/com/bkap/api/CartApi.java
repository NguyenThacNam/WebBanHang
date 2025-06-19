package com.bkap.api;

import com.bkap.service.CartService;
import com.cart.CartItem;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")

public class CartApi {

    @Autowired
    private CartService cartService;
    
   
    
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(HttpSession session) {
        return ResponseEntity.ok(cartService.getCart(session));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(HttpSession session, @RequestBody CartItem item) {
        cartService.addToCart(session, item);
        return ResponseEntity.ok("Thêm vào giỏ hàng thành công!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCart(HttpSession session, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateQuantity(session, productId, quantity);
        return ResponseEntity.ok("Cập nhật thành công!");
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeItem(HttpSession session, @PathVariable Long productId) {
        cartService.removeFromCart(session, productId);
        return ResponseEntity.ok("Xóa thành công!");
    }
}
