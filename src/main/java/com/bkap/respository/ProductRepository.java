package com.bkap.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bkap.entity.Category;
import com.bkap.entity.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	  List<Product> findByNameContaining(String keyword);
	  List<Product> findTop12ByOrderByCreatedAtDesc(); //12 sản phẩm mới
	  List<Product> findByCategory(Category category);// Sản phẩm liên quan
	  
	  
}  

