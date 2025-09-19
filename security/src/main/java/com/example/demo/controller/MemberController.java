package com.example.demo.controller;

import com.example.demo.model.Member;
import com.example.demo.model.MemberUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    // 컨트롤러 내부에서도 로그인한 사용자 정보에 접근할 수 있다
    // 커스텀 UserDetails도 지원한다.
    //   MemberUserDetails memberUserDetails...
    //   log.info("{}", userDetails.getDisplayName());
    @GetMapping("/member/list/1")    public String getMembers1(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // userDetails is null if not login
        if (userDetails != null) {
            log.info("{}", userDetails.getUsername());
            log.info("{}", userDetails.isEnabled());
            log.info("{}", userDetails.isAccountNonLocked());
            log.info("{}", userDetails.isAccountNonExpired());
            log.info("{}", userDetails.isCredentialsNonExpired());
        } else {
            log.info("userDetails is null");
        }

        model.addAttribute("members", members);
        return "/member-list";
    }
}
