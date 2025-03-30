package org.task4.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    //метод инициализации БД
    public static void initialize() {
        try (Connection connection = DBConfig.getConnection();
             Statement statement = connection.createStatement()) {

            // Выполняем SQL-скрипт для создания таблицы
            statement.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
