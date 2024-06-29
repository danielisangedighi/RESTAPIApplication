package com.example.RESTAPIApplication.service;

import com.example.RESTAPIApplication.model.User;
import com.example.RESTAPIApplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        User foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    public void testExistsByEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        boolean exists = userService.existsByEmail("test@example.com");

        assertEquals(true, exists);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    public void testFindAll() {
        userService.findAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteUserById() {
        userService.deleteUserById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
