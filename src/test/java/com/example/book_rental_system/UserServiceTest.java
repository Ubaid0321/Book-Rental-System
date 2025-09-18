package com.example.book_rental_system;

import com.example.book_rental_system.dto.UserDto;
import com.example.book_rental_system.entity.User;
import com.example.book_rental_system.exception.ResourceNotFoundException;
import com.example.book_rental_system.repositry.UserRepository;
import com.example.book_rental_system.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUserSuccess() {
        UserDto dto = new UserDto(8L,"John", "john@example.com", "USER");
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .role("USER")
                .build();

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("USER", result.getRole());

        verify(userRepository).existsByEmail("john@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUserEmailExists() {
        UserDto dto = new UserDto(8L,"John", "john@example.com", "USER");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));

        verify(userRepository).existsByEmail("john@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserFound() {
        User user = User.builder().id(1L).name("John").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUser(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());

        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(1L));

        verify(userRepository).findById(1L);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(
                User.builder().id(1L).build(),
                User.builder().id(2L).build()
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }
}
