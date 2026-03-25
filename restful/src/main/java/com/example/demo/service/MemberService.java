package com.example.demo.service;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse create(MemberRequest memberRequest) {
        Member member =  Member.builder()
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .age(memberRequest.getAge())
                .enabled(true).build(); // default enabled is true
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    @Transactional
    public List<MemberResponse> createList(List<MemberRequest> memberRequests) {
        List<MemberResponse> memberResponseList = memberRequests.stream().map(this::create).toList();
        return memberResponseList;
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapToMemberResponse(member);
    }

    public List<MemberResponse> findAll_() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponse> result = new ArrayList<>();
        for (Member member: members) {
            MemberResponse response = mapToMemberResponse(member);
            result.add(response);
        }
        return result;
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream().map(this::mapToMemberResponse).toList();
    }

    public List<MemberResponse> findAll(String name) {
        if (name == null || name.isBlank()) {
            return memberRepository
                    .findAll()
                    .stream()
                    .map(this::mapToMemberResponse)
                    .toList();
        } else {
            return memberRepository
                    .findByNameContainingOrderByNameAsc(name)
                    .stream()
                    .map(this::mapToMemberResponse)
                    .toList();
        }
    }

    public MemberResponse update(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        // password, enabled 필드를 유지해야 하므로 나머지 항목들만 업데이트
        member.setName(memberRequest.getName());
        member.setEmail(memberRequest.getEmail());
        member.setAge(memberRequest.getAge());
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    public MemberResponse patch(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        // 전달된 값이 있는 필드만 업데이트
        if (memberRequest.getName() != null) member.setName(memberRequest.getName());
        if (memberRequest.getEmail() != null) member.setEmail(memberRequest.getEmail());
        if (memberRequest.getAge() != null) member.setAge(memberRequest.getAge());
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    public void deleteById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        memberRepository.delete(member);
    }

    private MemberResponse mapToMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge())
                .build();
    }
}
