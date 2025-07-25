package com.bkap.service;

import com.bkap.entity.User;
import com.bkap.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserLoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username); // tim theo ten dg n
        if (user == null) throw new UsernameNotFoundException("Không tìm thấy user");

        return new org.springframework.security.core.userdetails.User(  // UserDetails của Spring Security
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(() -> user.getRole()) // 
        );
    }

}
