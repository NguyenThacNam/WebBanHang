package com.bkap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.bkap.entity.Category;
import com.bkap.entity.Product;
import com.bkap.service.CategoryService;
import com.bkap.service.ProductService;

@Controller
public class ProductListController {
	@Autowired
    private ProductService proSv;
	@Autowired CategoryService catSv;
	@GetMapping("/products")
	public String productList(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "8") int size,
	    @RequestParam(required = false) Long categoryId,
	    Model model) {

	    Page<Product> productPage; // lưu sp pt

	    if (categoryId != null) {
	        Category selectedCategory = new Category(); // dtg dm
	        selectedCategory.setId(categoryId); // thiet lap id
	        List<Product> productsByCategory = proSv.getProductsByCategory(selectedCategory);
	        int start = page * size;
	        int end = Math.min(start + size, productsByCategory.size());
	        List<Product> pageContent = productsByCategory.subList(start, end); // sp trang hien tai
	        productPage = new org.springframework.data.domain.PageImpl<>(pageContent, PageRequest.of(page, size), productsByCategory.size());
	        model.addAttribute("selectedCategoryId", categoryId);
	    } else {
	        productPage = proSv.getPaginatedProducts(page, size);
	    }

	    model.addAttribute("products", productPage.getContent()); // danh sách sản phẩm hiện tại
	    model.addAttribute("currentPage", page);//trang hiện tại
	    model.addAttribute("totalPages", productPage.getTotalPages());//tổng số trang
	    model.addAttribute("categories", catSv.getAllCategories());
	    return "site/product-list";
	}


	   

}
