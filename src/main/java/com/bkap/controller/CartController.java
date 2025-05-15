package com.bkap.controller;

import com.bkap.service.CartService;
import com.cart.CartItem;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	 
	@GetMapping
	public String viewCart(HttpSession session, Model model) {
		model.addAttribute("cartItems", cartService.getCart(session));
		model.addAttribute("total", cartService.getTotal(session));
		return "site/cart"; // cart.html
	}

	@PostMapping("/add")
	public String addToCart(HttpSession session, @RequestParam Long productId, @RequestParam String productName,
			@RequestParam String image, @RequestParam int quantity, @RequestParam double price) {

		CartItem item = new CartItem(productId, productName, image, quantity, price);
		cartService.addToCart(session, item);
		return "redirect:/cart";
	}

	@PostMapping("/update")
	public String updateCart(HttpSession session, @RequestParam Long productId, @RequestParam int quantity) {
		cartService.updateQuantity(session, productId, quantity); // Cập nhật số lượng sản phẩm trong giỏ
		return "redirect:/cart"; // Chuyển hướng lại trang giỏ hàng
	}

	@GetMapping("/remove/{productId}")
	public String removeItem(HttpSession session, @PathVariable Long productId) {
		cartService.removeFromCart(session, productId);
		return "redirect:/cart";
	}
}