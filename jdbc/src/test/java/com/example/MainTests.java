package com.example;

import org.junit.jupiter.api.Test;

import java.sql.*;

public class MainTests {
    @Test
    public void testJdbcConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:mydb", "sa", "");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT H2VERSION()");

        if (resultSet.next()) {
            String version = resultSet.getString(1);
            System.out.println("H2 Database Version = " + version);
        }

        resultSet.close();
        statement.close();

        connection.close();
    }
}
