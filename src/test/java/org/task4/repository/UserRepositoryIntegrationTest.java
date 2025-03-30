package org.task4.repository;

import org.junit.jupiter.api.*;
import org.task4.database.DBConfig;
import org.task4.database.DBInitializer;
import org.task4.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserRepositoryIntegrationTest {

    private UserRepository userRepository;
    private User user;
    private Connection connection;

    @BeforeEach
    void setup() throws SQLException {
        this.connection = DBConfig.getConnection();
        this.userRepository = new UserRepository();
        DBInitializer.initialize();

        this.user = new User("First Name", "Last Name", "test@mail");
    }

    @AfterEach
    void dropTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE users");
        }
        connection.close();
    }

    @Test
    void saveUserTest() throws SQLException {
        boolean result = userRepository.save(user);

        assertTrue(result);

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            statement.setString(1, user.getEmail());
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals(user.getId(), resultSet.getLong("id"));
                assertEquals(user.getFirstName(), resultSet.getString("first_name"));
                assertEquals(user.getLastName(), resultSet.getString("last_name"));
            }
        }
    }

    @Test
    void findUserByIdTest() {
        userRepository.save(user);

        Optional<User> result = userRepository.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findAllUsersTest() {
        User user2 = new User("First Name2", "Last Name2", "test2@mail");
        userRepository.save(user);
        userRepository.save(user2);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        List<User> result = userRepository.findAll();

        assertEquals(users, result);
    }

    @Test
    void updateUserTest() {
        userRepository.save(user);
        user.setEmail("1234");
        userRepository.update(user);
        Optional<User> result = userRepository.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void deleteUserTest() {
        userRepository.save(user);
        userRepository.delete(user.getId());
        Optional<User> result = userRepository.findById(user.getId());

        assertTrue(result.isEmpty());
    }
}
