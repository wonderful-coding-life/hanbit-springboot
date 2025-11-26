package com.example.demo;

import com.example.demo.config.MyRestClient;
import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

// RestTemplate -> WebClient -> RestClient(blocking만 지원)로 발전
// RestClient는 RestTemplate의 공식적인 미래, WebClient보다 가볍고 RestTemplate보다 현대적
// Test 수행할 때에는 실제 port로 Tomcat이 실행되는 것이 아니므로 막아 둔다.
//@Component
@Slf4j
public class RestApplication implements ApplicationRunner {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient webClient;

    @Autowired
    private MyRestClient myRestClient; // RestClient + HttpServiceProxyFactory(@HttpExchange)

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 가장 심플
        // @HttpExchange로 MyRestClient 인터페이스 정의
        // @Configuration에서 RestClient를 사용하여 Bean 등록
        testMyRestClient();

        var memberId = postMember();
        if (memberId != null) {
            var memberResponse = getMember(memberId);
            log.info("{}", memberResponse);
        }

        memberId = postMemberWebClient();
        if (memberId != null) {
            var memberResponse = getMemberWebClient(memberId);
            log.info("{}", memberResponse);
        }
    }

    private Long postMember() {
        var memberRequest = MemberRequest.builder()
                .name("윤서준")
                .email("SeojunYoon@hanbit.co.kr")
                .age(10).build();

        var postResponse = restTemplate.postForEntity("http://localhost:8080/api/members",
                memberRequest, MemberResponse.class);

        log.info("{}", postResponse.getStatusCode());

        if (postResponse.getStatusCode().is2xxSuccessful()) {
            log.info("{}", postResponse.getBody());
            if (postResponse.getBody() != null) {
                return postResponse.getBody().getId();
            }
        }
        return null;
    }

    private MemberResponse getMember(Long memberId) {
        var getResponse = restTemplate.getForEntity("http://localhost:8080/api/members/" + memberId, MemberResponse.class);

        log.info("{}", getResponse.getStatusCode());

        if (getResponse.getStatusCode().is2xxSuccessful()) {
            return getResponse.getBody();
        }
        return null;
    }

    private Long postMemberWebClient() {
        var memberRequest = MemberRequest.builder()
                .name("윤광철")
                .email("KwangcheolYoon@hanbit.co.kr")
                .age(43).build();

        var postResponse = webClient.post()
                .uri("/api/members")
                .bodyValue(memberRequest)
                .retrieve()
                .toEntity(MemberResponse.class)
                .block();

        if (postResponse != null) {
            log.info("{}", postResponse.getStatusCode());
            log.info("{}", postResponse.getBody());
            if (postResponse.getBody() != null) {
                return postResponse.getBody().getId();
            }
        }
        return null;
    }

    private MemberResponse getMemberWebClient(Long memberId) {
        var postResponse = webClient.get()
                .uri("/api/members/" + memberId)
                .retrieve()
                .toEntity(MemberResponse.class)
                .block();

        if (postResponse != null) {
            log.info("{}", postResponse.getStatusCode());
            return postResponse.getBody();
        }
        return null;
    }

    private void testMyRestClient() {
        List<MemberResponse> memberResponses = myRestClient.getMembers();
        memberResponses.forEach(member -> log.info(">> 회원 {}", member));
    }
}
