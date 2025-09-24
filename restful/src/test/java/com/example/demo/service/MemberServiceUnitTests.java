package com.example.demo.service;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MemberServiceUnitTests {
    @MockitoBean
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    public void findById() {
        // when().thenReturn()으로 여러개의 조건을 만들 수 있다.
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(Member.builder()
                .id(1L)
                .name("윤서준")
                .email("SeojunYoon@hanbit.co.kr")
                .age(10).build()));
        when(memberRepository.count()).thenReturn(2L);

        MemberResponse memberResponse = memberService.findById(1L);

        assertThat(memberResponse.getId()).isEqualTo(1L);
        assertThat(memberResponse.getName()).isEqualTo("윤서준");
        assertThat(memberResponse.getEmail()).isEqualTo("SeojunYoon@hanbit.co.kr");
        assertThat(memberResponse.getAge()).isEqualTo(10);
    }
}
