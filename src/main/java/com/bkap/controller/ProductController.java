package com.bkap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bkap.entity.Category;
import com.bkap.entity.Product;
import com.bkap.service.CategoryService;
import com.bkap.service.ProductService;



@Controller
@RequestMapping("/admin/product")
public class ProductController {
	@Autowired
	private ProductService proSv;
	
	@Autowired
	private CategoryService cateSv;
	//Hiển thị
//   @GetMapping("")
//   public String list(Model model) {
//	   model.addAttribute("data" , proSv.getAllProducts());
//	   return "admin/product/list";
//	   
//   }
   //thêm
   @GetMapping("/create")
   public String create(Model model) {
       model.addAttribute("product", new Product());
       model.addAttribute("categories", cateSv.getAllCategories());
       return "admin/product/form";
   }

   @PostMapping("/save")
   public String saveProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile file,
                             @RequestParam("categoryId") Long categoryId) {

	   Category category = cateSv.getCategoryById(categoryId).get();
       product.setCategory(category); // gán category vào product
       proSv.saveProduct(product, file); // lưu
       return "redirect:/admin/product";
   }
  //sửa
   @GetMapping("/edit/{id}")
   public String showEditForm(@PathVariable Long id, Model model) {
       Product product = proSv.getProductById(id);
       model.addAttribute("product", product);
       model.addAttribute("categories", cateSv.getAllCategories());
       return "admin/product/edit";
   }
   
   @PostMapping("/update")
   public String updateProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile file, @RequestParam("categoryId") Long categoryId) {
       Category category = cateSv.getCategoryById(categoryId).get();
       product.setCategory(category);
       proSv.updateProduct(product, file);
       return "redirect:/admin/product";
   }
   //Xóa
   @GetMapping("/delete/{id}")
   public String deleteProduct(@PathVariable Long id) {
       proSv.deleteProduct(id);
       return "redirect:/admin/product";
   }
   //Tìm kiếm
  
   @GetMapping("/search")
   public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
       List<Product> products = proSv.searchProducts(keyword);
       model.addAttribute("data", products);
       model.addAttribute("keyword", keyword);
       return "admin/product/list";
   }
  
   //Phân trang
   @GetMapping("")
   public String list(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "5") int size,
           Model model) {

       Page<Product> productPage = proSv.getPaginatedProducts(page, size); // lấy tất cả sản ph

       model.addAttribute("data", productPage.getContent());  // danh sách sản phẩm hiện tại
       model.addAttribute("currentPage", page); //trang hiện tại
       model.addAttribute("totalPages", productPage.getTotalPages());//tổng số trang

       return "admin/product/list";
   }




  

}
