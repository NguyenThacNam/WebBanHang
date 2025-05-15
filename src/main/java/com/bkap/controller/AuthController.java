package com.bkap.controller;

import com.bkap.entity.User;
import com.bkap.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Hiển thị form login
    @GetMapping("/login")
    public String showLoginForm() {
        return "site/login"; 
    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "site/register"; 
    }

    // Xử lý form đăng ký
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "site/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        model.addAttribute("success", "Đăng ký thành công!");
        return "site/login";
    }
}
