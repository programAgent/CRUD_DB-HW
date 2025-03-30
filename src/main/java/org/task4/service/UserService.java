package org.task4.service;

import org.task4.model.User;
import org.task4.repository.UserRepository;

import java.util.List;
import java.util.Optional;


public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Сохранение нового пользователя в БД
    public boolean save(User user) {
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name can't be empty");
        }

        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name can't be empty");
        }

        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Incorrect email");
        }

        boolean notUniqueEmail = userRepository.findAll().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));

        if (notUniqueEmail) {
            throw new IllegalArgumentException("Email is already taken");
        }

        return userRepository.save(user);
    }

    // Поиск пользователя по ID
    public Optional<User> findByID(Long id) {
        return userRepository.findById(id);
    }

    // Вывести всех пользователей
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Обновить данные сохранённого пользователя
    public boolean update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User without ID for update");
        }

        if (userRepository.findById(user.getId()).isEmpty()) {
            throw new IllegalArgumentException("User with ID " + user.getId() + " is not founded");
        }

        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Incorrect email");
        }

        return userRepository.update(user);
    }

    // Удаление пользователя по ID
    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User without ID for delete");
        }
        if (userRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("User with ID " + id + " is not founded");
        }
        return userRepository.delete(id);
    }
}