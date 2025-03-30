package org.task4.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.task4.model.User;
import org.task4.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    User user = new User("FirstName", "LastName", "test@mail");
    User user2 = new User("FirstName2", "LastName2", "test@mail2");

    @Test
    void saveUserTest() {
        when(userRepository.save(user)).thenReturn(true);

        boolean result = userService.save(user); // допустим он ничего не возвращает, просто вызывает userRepository.save(user)

        assertTrue(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findUserById() {
        user.setId(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> found = userService.findByID(user.getId());

        assertTrue(found.isPresent());
        assertEquals(Optional.of(user), found);

        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void findUserByEmptyIdTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Optional<User> found = userService.findByID(user.getId());

        assertTrue(found.isEmpty());
        assertEquals(Optional.empty(), found);

        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void findAllUsersTest() {
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(users, result);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findAllUsersEmptyTest() {
        ArrayList<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(users, result);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUserTest() {
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.update(user)).thenReturn(true);

        boolean result = userService.update(user);

        assertTrue(result);

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).update(user);
    }

    @Test
    void deleteUserTest() {
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.delete(user.getId())).thenReturn(true);

        boolean result = userService.delete(user.getId());

        assertTrue(result);

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).delete(user.getId());
    }

    @Test
    void saveUserFirstNameNullExceptionTest() {
        user.setFirstName(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.save(user));

        assertEquals("First name can't be empty", exception.getMessage());
    }

    @Test
    void saveUserLastNameNullExceptionTest() {
        user.setLastName(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.save(user));

        assertEquals("Last name can't be empty", exception.getMessage());
    }

    @Test
    void saveUserWrongEmailExceptionTest() {
        user.setEmail("mail");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.save(user));

        assertEquals("Incorrect email", exception.getMessage());
    }

    @Test
    void saveUserUsedEmailExceptionTest() {
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.save(user));

        assertEquals("Email is already taken", exception.getMessage());
    }

    @Test
    void updateUserNullIdExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.update(user));

        assertEquals("User without ID for update", exception.getMessage());
    }

    @Test
    void updateUserIdNotFoundExceptionTest() {
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.update(user));

        assertEquals("User with ID " + user.getId() + " is not founded", exception.getMessage());
    }

    @Test
    void updateUserWithWrongEmail() {
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        user.setEmail("1234");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.update(user));

        assertEquals("Incorrect email", exception.getMessage());

    }

    @Test
    void deleteUserWithoutIdExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.delete(user.getId()));

        assertEquals("User without ID for delete", exception.getMessage());
    }

    @Test
    void deleteUserIdNotFoundExceptionTest() {
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.delete(user.getId()));

        assertEquals("User with ID " + user.getId() + " is not founded", exception.getMessage());
    }
}

