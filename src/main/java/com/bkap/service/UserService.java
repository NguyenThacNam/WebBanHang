package com.bkap.service;

import com.bkap.entity.User;
import com.bkap.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
