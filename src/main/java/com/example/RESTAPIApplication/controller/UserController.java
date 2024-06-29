package com.example.RESTAPIApplication.controller;

import com.example.RESTAPIApplication.model.User;
import com.example.RESTAPIApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String viewUserProfile(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "welcome";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam Long id, @RequestParam String email, @RequestParam String password) {
        User user = userService.findById(id);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userService.saveUser(user);
        return "redirect:/users/profile";
    }

    @GetMapping("/admin")
    public String viewAllUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "adminWelcome";
    }

    @PostMapping("/admin/update")
    public String updateUserAsAdmin(@RequestParam Long id, @RequestParam String email, @RequestParam String password) {
        User user = userService.findById(id);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userService.saveUser(user);
        return "redirect:/users/admin";
    }

    @DeleteMapping("/admin/delete/{id}")
    public String deleteUserAsAdmin(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users/admin";
    }
}
