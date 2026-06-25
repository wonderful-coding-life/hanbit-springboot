package com.example;

import com.example.model.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/*
    Class.forName("com.mysql.cj.jdbc.Driver"); java 8, jdbc 4 이후에는 필요 없음
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "myuser", "mypass");
    connection.setAutoCommit(false)
    do something
    connection.commit()
    connection.rollback()
    connection.close()

    Statement statement = connection.createStatement();
    statement.executeQuery(SQL 문) for SELECT
    statement.executeUpdate(SQL 문) for INSERT, UPDATE, DELETE
    statement.execute(SQL 문) for others

    prepareStatement()에서 SQL문이 고정
    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO member(name, email, age) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(index, param); 1부터 시작하는 파라미터 인덱스, 파라미터 값
    preparedStatement.setInt(...);
    preparedStatement.setLong(...);
    preparedStatement.executeQuery(); for SELECT
    preparedStatement.executeUpdate(); for INSERT, UPDATE, DELETE
 */

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

        insertMember(connection, "윤서준", "SeojunYoon@hanbit.co.kr", 10);
        insertMember(connection, "윤광철", "KwangcheolYoon@hanbit.co.kr", 43);
        insertMember(connection, "공미영", "MiyeongKong@hanbit.co.kr", 23);

        Long id = insertMemberReturningGeneratedKey(connection, "김도윤", "DoyunKim@hanbit.co.kr", 10);

        var member = selectMemberById(connection, id);
        if (member != null) {
            member.setAge(11);
            updateMember(connection, member);
        }

        deleteMember(connection, id - 1);

        //selectMember(connection);
        selectAll(connection);

        connection.close();
    }

    private void dropTable(Connection connection) throws SQLException {
        String dropTable = "DROP TABLE IF EXISTS member;";
        try (Statement statement = connection.createStatement()) {
            statement.execute(dropTable);
        }
    }

    private void createTable(Connection connection) throws SQLException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS member (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    email VARCHAR(256) NOT NULL UNIQUE,
                    age INTEGER
                );
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTable);
            }
    }

    private void createTablePostgres(Connection connection) throws SQLException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS member (
                    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    email VARCHAR(256) NOT NULL UNIQUE,
                    age INTEGER
                );
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTable);
        }
    }

    private int insertMember(Connection connection, String name, String email, Integer age) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO member(name, email, age) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, age);
            return preparedStatement.executeUpdate();
        }
    }

    private Long insertMemberReturningGeneratedKey(Connection connection, String name, String email, Integer age) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO member(name, email, age) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
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
    }

    private int updateMember(Connection connection, Member member) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE member SET name = ?, email = ?, age = ? WHERE id = ?")) {
            preparedStatement.setString(1, member.getName());
            preparedStatement.setString(2, member.getEmail());
            preparedStatement.setInt(3, member.getAge());
            preparedStatement.setLong(4, member.getId());
            return preparedStatement.executeUpdate();
        }
    }

    private int deleteMember(Connection connection, Long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM member WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate();
        }
    }

    private void selectAll(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM member")) {
            while (resultSet.next()) {
                var member = new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getInt("age"));
                log.info("회원 {}", member);
            }
        }
    }

    private Member selectMemberById(Connection connection, Long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE id=?")) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
    }
}