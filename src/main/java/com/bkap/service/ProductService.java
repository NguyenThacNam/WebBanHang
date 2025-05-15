package com.bkap.service;

import org.springframework.data.domain.Pageable; // ✅ ĐÚNG

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bkap.entity.Category;
import com.bkap.entity.Product;
import com.bkap.respository.ProductRepository;



@Service
public class ProductService {
   @Autowired
   private ProductRepository proRepo;
   
   public List<Product> getAllProducts() {
	   return proRepo.findAll();
   }
   //Thêm
   public void saveProduct(Product product, MultipartFile file) {
	    try {
	        if (!file.isEmpty()) {
	            String fileName = file.getOriginalFilename();
	            Path path = Paths.get("images/" + fileName);
	            Files.write(path, file.getBytes()); // Lưu ảnh vào thư mục images
	            product.setImage(fileName); // Chỉ lưu tên file vào database
	        }
	        proRepo.save(product);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
   //Sửa
   public void updateProduct(Product product, MultipartFile file) {
	    try {
	        if (!file.isEmpty()) {
	            String fileName = file.getOriginalFilename();
	            Path path = Paths.get("images/" + fileName);
	            Files.write(path, file.getBytes());  // Cập nhật ảnh mới
	            product.setImage(fileName);
	        }
	        proRepo.save(product);  // Cập nhật sản phẩm trong DB
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
   
   public Product getProductById(Long id) {
	    return proRepo.findById(id).orElse(null);
	}
  //Xóa
   public void deleteProduct(Long id) {
	   proRepo.deleteById(id);
	}
   //Tìm kiếm
   public List<Product> searchProducts(String keyword) {
	    return proRepo.findByNameContaining(keyword);
	}
   //Phân trang 
   public Page<Product> getPaginatedProducts(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    return proRepo.findAll(pageable); // lấy tấy cả sản phẩm theo từng trang 
	}
  //Hiển thị sản phẩm mới
   public List<Product> getLatestProducts() {
       return proRepo.findTop12ByOrderByCreatedAtDesc();
   }
   //Sản phẩm liên quan
   public List<Product> getProductsByCategory(Category category) {
	    return proRepo.findByCategory(category);
	}
 

 


}
