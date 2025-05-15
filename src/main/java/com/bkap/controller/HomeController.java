package com.bkap.controller;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bkap.entity.Product;
import com.bkap.entity.User;
import com.bkap.respository.UserRepository;
import com.bkap.service.ProductService;

@Controller
public class HomeController {

    @Autowired
    private ProductService proSv;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("")
    public String home(Model model, Principal principal) {
        List<Product> latestProducts = proSv.getLatestProducts();
        model.addAttribute("data", latestProducts);

        // Nếu người dùng đã đăng nhập
        if (principal != null) {
            String username = principal.getName();
            User user = userRepo.findByUsername(username);
            model.addAttribute("userInfo", user);
        }

        return "site/home";
    }

    @GetMapping("/about")
    public String about() {
        return "site/about";
    }
}
