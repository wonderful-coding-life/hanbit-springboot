package com.example.demo.controller;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 응답 본문을 EUC-KR로 반환하는 경우 주의사항
// - application/json, application/xml 형식은 일반적으로 UTF-8 기반으로 동작하며
//   Spring의 기본 MessageConverter에서는 EUC-KR 응답을 정상 지원하지 않음
// - 따라서 EUC-KR 응답은 보통 text/plain 타입으로 처리해야 함
//
// 예:
// @GetMapping(
//     value = "/sample",
//     produces = "text/plain; charset=euc-kr"
// )
//
// 컨트롤러에서 String을 반환하면
// StringHttpMessageConverter가 동작하면서
// EUC-KR charset으로 인코딩하여 응답 본문을 반환함
//
// 예:
// return "한글 응답";
//
// 꼭 JSON/XML 형식이 필요하다면
// ObjectMapper 또는 XmlMapper를 직접 사용하여
// 객체를 JSON/XML 문자열로 변환한 뒤 String으로 반환 가능
//
// 예:
// ObjectMapper mapper = new ObjectMapper();
// String json = mapper.writeValueAsString(obj);
//
// XmlMapper xmlMapper = new XmlMapper();
// String xml = xmlMapper.writeValueAsString(obj);
//
// 이후 produces="text/plain; charset=euc-kr" 로 설정하여
// EUC-KR 인코딩 문자열 형태로 응답 가능
//
// 즉:
// - 자동 JSON/XML 직렬화 → EUC-KR 지원 어려움
// - 수동 문자열 직렬화(String 반환) → EUC-KR 응답 가능

@RestController
public class TestController {
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/api/v3/members/{id}")
    public String getMember(@PathVariable("id") Long id, @RequestParam(value = "type", required = false) String type) throws JsonProcessingException {
        var member = memberRepository.findById(id).orElseThrow();
        if ("xml".equalsIgnoreCase(type)) {
            var mapper = new XmlMapper();
            return mapper.writeValueAsString(member);
        } else {
            var mapper = new ObjectMapper();
            return mapper.writeValueAsString(member);
        }
    }
}
