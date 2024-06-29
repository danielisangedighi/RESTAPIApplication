package com.example.RESTAPIApplication.controller;

import com.example.RESTAPIApplication.model.User;
import com.example.RESTAPIApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email, @RequestParam String password, Model model) {
        if (userService.existsByEmail(email)) {
            model.addAttribute("error", "Email already in use");
            return "register";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCategory("basic_user");
        userService.saveUser(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.findByEmail(email);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        model.addAttribute("user", user);
        if ("admin_user".equals(user.getCategory())) {
            return "redirect:/admin/welcome";
        } else {
            return "redirect:/welcome";
        }
    }
}
