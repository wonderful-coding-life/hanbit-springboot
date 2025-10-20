package com.example;

import com.example.model.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        var main = new Main();
        main.testJdbc();
    }

    private void testJdbc() throws SQLException, ClassNotFoundException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "myuser", "mypass");
//        dropTable(connection);
//        createTable(connection);

//        Class.forName("org.postgresql.Driver");
//        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mydb", "myuser", "mypass");
//        dropTable(connection);
//        createTablePostgres(connection);

        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:mydb", "sa", "");
        createTable(connection);

        insertMember(connection, "윤서준", "SeojoonYun@hanbit.co.kr", 10);
        insertMember(connection, "윤광철", "KwangcheolYoon@hanbit.co.kr", 43);
        insertMember(connection, "공미영", "MiyeongKong@hanbit.co.kr", 23);

        Long id = insertMemberReturningGeneratedKey(connection, "김도윤", "DoyunKim@hanbit.co.kr", 10);

        var member = selectMember(connection, id);
        if (member != null) {
            member.setAge(11);
            updateMember(connection, member);
        }

        deleteMember(connection, id - 1);

        selectMember(connection);
    }

    private void dropTable(Connection connection) throws SQLException {
        String dropTable = "DROP TABLE IF EXISTS member;";

        Statement statement = connection.createStatement();
        statement.execute(dropTable);
    }

    private void createTable(Connection connection) throws SQLException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS member (
                    id INTEGER AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    email VARCHAR(256) NOT NULL UNIQUE,
                    age INTEGER
                );
                """;

        Statement statement = connection.createStatement();
        statement.execute(createTable);
    }

    private void createTablePostgres(Connection connection) throws SQLException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS member (
                    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    email VARCHAR(256) NOT NULL UNIQUE,
                    age INTEGER
                );
                """;

        Statement statement = connection.createStatement();
        statement.execute(createTable);
    }

    private int insertMember(Connection connection, String name, String email, Integer age) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO member(name, email, age) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.setInt(3, age);

        return preparedStatement.executeUpdate();
    }

    private Long insertMemberReturningGeneratedKey(Connection connection, String name, String email, Integer age) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO member(name, email, age) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.setInt(3, age);

        int rowsInserted = preparedStatement.executeUpdate();
        if (rowsInserted > 0) {
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("생성된 키가 없습니다");
            }
        } else {
            throw new SQLException("생성할 수 없습니다");
        }
    }

    private int updateMember(Connection connection, Member member) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE member SET name = ?, email = ?, age = ? WHERE id = ?");
        preparedStatement.setString(1, member.getName());
        preparedStatement.setString(2, member.getEmail());
        preparedStatement.setInt(3, member.getAge());
        preparedStatement.setLong(4, member.getId());

        return preparedStatement.executeUpdate();
    }

    private int deleteMember(Connection connection, Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM member WHERE id = ?");
        preparedStatement.setLong(1, id);
        return preparedStatement.executeUpdate();
    }

    private void selectMember(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            var member = new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getInt("age"));
            log.info("회원 {}", member);
        }
    }

    private Member selectMember(Connection connection, Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE id=?");
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getInt("age"));
        } else {
            return null;
        }
    }
}