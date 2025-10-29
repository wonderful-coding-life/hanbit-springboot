package com.example.demo;

import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SpringJdbcApplication implements ApplicationRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // insert
        String sql = "INSERT INTO member (name, email, age) VALUES (?, ?, ?)";
        int rowCount = jdbcTemplate.update(sql, "홍길동", "GildongHong@hanbit.co.kr", 16);
        log.info("{}", rowCount);

        // named parameter jdbc template
        sql = "INSERT INTO member (name, email, age) VALUES (:name, :email, :age)";

        Map<String, Object> params = new HashMap<>();
        params.put("name", "홍길순");
        params.put("email", "GilsoonHong@hanbit.co.kr");
        params.put("age", 15);

        rowCount = namedParameterJdbcTemplate.update(sql, params);
        log.info("{}", rowCount);

        MapSqlParameterSource paramsSource = new MapSqlParameterSource()
                .addValue("name", "홍길철")
                .addValue("email", "GilcheolHong@hanbit.co.kr")
                .addValue("age", 15);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        rowCount = namedParameterJdbcTemplate.update(sql, paramsSource, keyHolder, new String[] {"id"});
        log.info("{} {}", rowCount, keyHolder.getKey().longValue());

        Member member = Member.builder()
                .name("홍길자")
                .email("GiljaHong@hanbit.co.kr")
                .age(21).build();
        BeanPropertySqlParameterSource beanParamSource = new BeanPropertySqlParameterSource(member);
        keyHolder = new GeneratedKeyHolder();
        rowCount = namedParameterJdbcTemplate.update(sql, beanParamSource, keyHolder, new String[] {"id"});
        log.info("{} {}", rowCount, keyHolder.getKey().longValue());

        // select all
        sql = "SELECT * FROM member";
        List<Member> members = jdbcTemplate.query(sql, (rs, rowNum) -> Member.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .age(rs.getInt("age")).build());
        log.info("{}", members);


    }
}
