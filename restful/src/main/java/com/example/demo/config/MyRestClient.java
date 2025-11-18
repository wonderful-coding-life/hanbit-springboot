package com.example.demo.config;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange
public interface MyRestClient {
    @GetExchange("/api/members")
    List<MemberResponse> getMembers();

    @GetExchange("/api/members/{id}")
    MemberResponse getMemberById(@PathVariable Long id);

    @PostExchange("/api/members")
    MemberResponse postMember(@RequestBody MemberRequest memberRequest);
}
