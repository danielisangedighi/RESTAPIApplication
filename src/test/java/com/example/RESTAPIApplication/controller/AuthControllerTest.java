package com.example.RESTAPIApplication.controller;

import com.example.RESTAPIApplication.model.User;
import com.example.RESTAPIApplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new BindingAwareModelMap();
    }

    @Test
    public void testRegisterUser_Success() {
        when(userService.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        String viewName = authController.registerUser("test@example.com", "password", model);

        verify(userService, times(1)).saveUser(any(User.class));
        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testRegisterUser_EmailExists() {
        when(userService.existsByEmail(anyString())).thenReturn(true);

        String viewName = authController.registerUser("test@example.com", "password", model);

        verify(userService, never()).saveUser(any(User.class));
        assertEquals("register", viewName);
    }

    @Test
    public void testLoginUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setCategory("basic_user");

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        String viewName = authController.loginUser("test@example.com", "password", model);

        assertEquals("redirect:/welcome", viewName);
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        when(userService.findByEmail(anyString())).thenReturn(null);

        String viewName = authController.loginUser("test@example.com", "password", model);

        assertEquals("login", viewName);
    }
}
