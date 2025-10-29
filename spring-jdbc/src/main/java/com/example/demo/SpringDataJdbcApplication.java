package com.example.demo;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpringDataJdbcApplication implements ApplicationRunner {
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberRepository.save(Member.builder()
                .name("정수빈")
                .email("SubinJung@hanbit.co.kr")
                .age(10).build());
        memberRepository.save(Member.builder()
                .name("윤지웅")
                .email("JieungYoon@hanbit.co.kr")
                .age(10).build());
        var members = memberRepository.findAll();
        log.info("{}", members);

        long count = memberRepository.count();
        log.info("count = {}", count);

        Member member = memberRepository.findByName("윤지웅").getFirst();
        log.info("member = {}", member);
        memberRepository.delete((member));

        member = memberRepository.findByNameOrEmail("윤서준", "SubinJung@hanbit.co.kr").getFirst();
        log.info("member = {}", member);
        memberRepository.delete((member));
    }
}
