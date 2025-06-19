package com.bkap.controller;

import com.bkap.entity.User;
import com.bkap.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("admin", new User());
        return "admin/register"; 
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("admin") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_ADMIN"); 
        userRepo.save(user);
        return "redirect:/admin/login";
    }

}
