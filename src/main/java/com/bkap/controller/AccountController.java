package com.bkap.controller;

import com.bkap.entity.User;
import com.bkap.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/account")
    public String accountDetail(Model model, Principal principal) {
        if (principal == null) { // người dùng chưa đăng nhập
            return "redirect:/login";
        }

        String username = principal.getName(); // Lấy username đang đăng nhập
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "redirect:/login?error=user-not-found"; // Trường hợp username không tồn tại trong DB
        }

        model.addAttribute("user", user);
        return "site/account"; // Trả về trang chi tiết tài khoản
    }
    
  
}
