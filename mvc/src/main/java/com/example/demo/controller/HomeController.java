package com.example.demo.controller;

import com.example.demo.dto.MemberForm;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final MemberRepository memberRepository;

    @GetMapping
    public String getHome() {
        return "forward:/member/list";
    }

    @GetMapping("/signup")
    public String getMemberAdd(@ModelAttribute("memberForm") MemberForm memberForm) {
        return "signup";
    }

    @PostMapping("/signup")
    public String postMemberAdd(@Valid @ModelAttribute("memberForm") MemberForm memberForm, BindingResult bindingResult) {
        // 패스워드 확인 검증
        if (!Objects.equals(memberForm.getPassword(), memberForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "error.signup.password.mismatch", "비밀번호가 일치하지 않습니다.");
        }

        // 이메일 중복 검증
        if (memberRepository.findByEmail(memberForm.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "AlreadyExist", "사용중인 이메일입니다");
        }

        // 에러가 있다면 다시 입력
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // 검증을 모두 통과했다면 저장하고 홈 화면으로 리다이렉트
        memberRepository.save(Member.builder()
                .name(memberForm.getName())
                .email(memberForm.getEmail())
                .password(memberForm.getPassword()).build());
        return "redirect:/";
    }
}
