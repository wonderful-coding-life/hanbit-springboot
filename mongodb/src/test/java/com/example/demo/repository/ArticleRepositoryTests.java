package com.example.demo.repository;

import com.example.demo.model.Member;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ArticleRepositoryTests {
    private static final Logger log = LoggerFactory.getLogger(ArticleRepositoryTests.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testFindByName() {
        List<Member> members = articleRepository.findByName("윤서준");
        members.forEach(member -> log.info("{}", member));
    }
}
