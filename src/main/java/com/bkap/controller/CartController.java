package com.bkap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bkap.service.CartService;
import com.bkap.service.CategoryService;

@Controller

public class CartController {

	@Autowired
	
	private CartService  cartService;
	
	@GetMapping("/cart")
	public String cart() {
		return "site/cart";
	}

}