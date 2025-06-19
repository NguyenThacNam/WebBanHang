package com.bkap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bkap.entity.User;

@Controller
@RequestMapping("admin")
public class AdminController {
  @GetMapping({"" , "/"})
  public String dashboard() {
	  return "admin/dashboard";
  }
  @GetMapping("/login")
  public String login() {
	  return "admin/login";
  }


}
