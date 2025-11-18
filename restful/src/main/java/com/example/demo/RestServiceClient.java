package com.example.demo;

import com.example.demo.dto.MemberResponse;
import com.example.demo.config.MyRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RestServiceClient implements ApplicationRunner {
    @Autowired
    private MyRestClient myRestClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MemberResponse> memberResponses = myRestClient.getMembers();
        memberResponses.forEach(member -> log.info(">> 회원 {}", member));
    }
}
