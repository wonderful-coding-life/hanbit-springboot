package com.example.demo.service;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class MemberServiceUnitSpyTests {
    @MockitoSpyBean
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    public void findById() {
        doReturn(Optional.ofNullable(Member.builder()
                .id(1L)
                .name("윤서준")
                .email("SeojunYoon@hanbit.co.kr")
                .age(10).build())).when(memberRepository).findById(1L);

        MemberResponse memberResponse = memberService.findById(1L);

        assertThat(memberResponse.getId()).isEqualTo(1L);
        assertThat(memberResponse.getName()).isEqualTo("윤서준");
        assertThat(memberResponse.getEmail()).isEqualTo("SeojunYoon@hanbit.co.kr");
        assertThat(memberResponse.getAge()).isEqualTo(10);

        // This will pass only with @MockitoSpyBean not @MockitoBean
        Member member = memberRepository.save(Member.builder()
                .name("윤광철")
                .email("KwangcheolYoon@hanbit.co.kr")
                .age(43).build());
        assertThat(member.getId()).isNotZero();
    }
}
