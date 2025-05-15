package com.bkap.service;

import com.bkap.entity.Category;
import com.bkap.respository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository cateRepo;

    public List<Category> getAllCategories() {
        return cateRepo.findAll();
    }

    public void saveCategory(Category category) {
        cateRepo.save(category);
    }
    public Optional<Category> getCategoryById(Long id) {
        return cateRepo.findById(id);
    }

    public void updateCategory(Category category) {
        cateRepo.save(category); 
    }
    public List<Category> searchCategories(String keyword) {
        return cateRepo.findByNameContaining(keyword);
    }
    public void deleteCategory(Long id) {
        cateRepo.deleteById(id);
    }
    public Page<Category> getPaginatedCategory(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    return cateRepo.findAll(pageable); // lấy tấy cả sản phẩm theo từng trang 
	}
	
}   
