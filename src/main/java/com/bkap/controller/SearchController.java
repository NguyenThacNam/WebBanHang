package com.bkap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bkap.entity.Product;
import com.bkap.service.ProductService;

@Controller
@RequestMapping("/search")
public class SearchController {
	  @Autowired
	  private ProductService proSv;
	  @GetMapping("")
	    public String searchProducts(@RequestParam String keyword, Model model) {
	        List<Product> products = proSv.searchProducts(keyword);
	        model.addAttribute("products", products);
	        model.addAttribute("keyword", keyword);
	        return "site/search-results"; 
	    }
}
