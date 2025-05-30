package com.example.demo.controller;

import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class MemberController {
    private List<Member> members = List.of(
            new Member(1L, "윤서준", "SeojunYoon@hanbit.co.kr", null),
            new Member(2L, "윤광철", "KwangcheolYoon@hanbit.co.kr", null),
            new Member(3L, "공미영", "MiyeongKong@hanbit.co.kr", null),
            new Member(4L, "김도윤", "DoyunKim@hanbit.co.kr", null)
    );

    @GetMapping("/member/list")
    public String getMembers(Model model) {
        model.addAttribute("members", members);
        return "/member-list";
    }

    @GetMapping("/member/list/1")
    public String getMembers1(Model model, @AuthenticationPrincipal User user) {

        // user is null if not login
        if (user != null) {
            log.info("{}", user.getUsername());
            log.info("{}", user.isEnabled());
            log.info("{}", user.isAccountNonLocked());
            log.info("{}", user.isAccountNonExpired());
            log.info("{}", user.isCredentialsNonExpired());
        } else {
            log.info("user is null");
        }

        model.addAttribute("members", members);
        return "/member-list";
    }
}
