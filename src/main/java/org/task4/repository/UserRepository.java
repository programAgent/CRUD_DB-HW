package org.task4.repository;

import org.task4.database.DBConfig;
import org.task4.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class UserRepository {

    // Сохранение пользователя
    public boolean save(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email) VALUES (?, ?, ?)";

        try (Connection connection = DBConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            int changes = statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getLong(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return changes > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // поиск пользователя по ID в БД
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DBConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                } else return Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    // поиск всех пользователей из БД
    public ArrayList<User> findAll() {
        String sql = "SELECT * FROM users";
        ArrayList<User> usersList = new ArrayList<>();

        try (Connection connection = DBConfig.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                mapResultSetToUser(resultSet).ifPresent(usersList::add);
            }
            return usersList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    // обновление пользователя в БД
    public boolean update(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ? WHERE id = ?";

        try (Connection connection = DBConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setLong(4, user.getId());
            int changes = statement.executeUpdate();
            return changes > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // удаление объекта по ID
    public boolean delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DBConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int changes = statement.executeUpdate();
            return changes > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // внутренний метод для преобразования ResultSet в объект класса User
    private Optional<User> mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        return Optional.of(user);
    }
}
