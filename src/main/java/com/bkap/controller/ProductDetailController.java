package com.bkap.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bkap.entity.Product;
import com.bkap.service.ProductService;

@Controller
public class ProductDetailController {
	@Autowired
    private ProductService proSv;
	 @GetMapping("/product/{id}")
	  public String productDetail(@PathVariable Long id, Model model) {
	      Product product = proSv.getProductById(id);
	      
	      if (product.getCategory() != null) { 
	          List<Product> relatedProducts = proSv.getProductsByCategory(product.getCategory());
	          model.addAttribute("relatedProducts", relatedProducts);
	      } else {
	          model.addAttribute("relatedProducts", new ArrayList<>());
	      }

	      model.addAttribute("product", product);
	      return "site/product-detail"; 
	  }
} 
