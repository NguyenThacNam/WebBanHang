package com.bkap.controller;

import com.bkap.entity.Category;
import com.bkap.entity.Product;
import com.bkap.service.CategoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

//	@GetMapping("")
//	public String list(Model model) {
//		model.addAttribute("data", categoryService.getAllCategories());
//		return "admin/category/list";
//	}
    // Thêm
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("category", new Category());
		return "admin/category/form";
	}
	@PostMapping("/save")
	public String save(@ModelAttribute Category category) {
		categoryService.saveCategory(category);
		return "redirect:/admin/category";
	}
	
	//Sửa      
	@GetMapping("/edit/{id}")
	public String Edit(@PathVariable Long id, Model model) {
	    Category category = categoryService.getCategoryById(id).orElse(null);
	    model.addAttribute("category", category);
	    return "admin/category/form";
	}

	@PostMapping("/update")
	public String updateCategory(@ModelAttribute Category category) {
	    categoryService.updateCategory(category);
	    return "redirect:/admin/category";
	}
    //Tìm kiếm
	@GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("data", categoryService.searchCategories(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/category/list";
    }
	//Xóa 
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable Long id) {
	    categoryService.deleteCategory(id);
	    return "redirect:/admin/category"; 
	}
	//Phân trang
	   @GetMapping("")
	   public String list(
	           @RequestParam(defaultValue = "0") int page,
	           @RequestParam(defaultValue = "5") int size,
	           Model model) {

	       Page<Category> categoryPage = categoryService.getPaginatedCategory(page, size); // lấy tất cả sản ph

	       model.addAttribute("data", categoryPage.getContent());  // danh sách sản phẩm hiện tại
	       model.addAttribute("currentPage", page); //trang hiện tại
	       model.addAttribute("totalPages", categoryPage.getTotalPages());//tổng số trang

	       return "admin/category/list";
	   }

}
