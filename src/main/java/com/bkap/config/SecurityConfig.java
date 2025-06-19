package com.bkap.config;

import com.bkap.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserLoginService userLoginService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //  ADMIN
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**") // chỉ áp dụng cho /admin/
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/login", "/admin/register").permitAll() // Cho phép truy cập tự do các trang login, register 
                .anyRequest().hasRole("ADMIN") 
            )
            .formLogin(form -> form
                .loginPage("/admin/login") // trang login admin
                .loginProcessingUrl("/admin/login") // xử lý 
                .defaultSuccessUrl("/admin", true) // đăng nhập thành công -> về trang chủ admin
                .failureUrl("/admin/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout")
                .invalidateHttpSession(true) //Hủy session khi logout
                .deleteCookies("JSESSIONID") // Xóa cookie phiên làm việc
                .permitAll()
            )
            .userDetailsService(userLoginService);

        return http.build();
    }

    //  user
    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(userLoginService);

        return http.build();
    }
   

}
