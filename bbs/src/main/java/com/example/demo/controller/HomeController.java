package com.example.demo.controller;

import com.example.demo.dto.MemberForm;
import com.example.demo.dto.PasswordForm;
import com.example.demo.model.MemberUser;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping
    public String getHome() {
        //return "redirect:/article/list";
        return "forward:/article/list";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String getLogout() {
        return "logout";
    }

    @GetMapping("/signup")
    public String getMemberAdd(@ModelAttribute("member") MemberForm memberForm) {
        return "signup";
    }

    @PostMapping("/signup")
    public String postMemberAdd(@Valid @ModelAttribute("member") MemberForm memberForm, BindingResult bindingResult) {
        if (memberForm.getPassword() == null || memberForm.getPassword().trim().length() < 8) {
            bindingResult.rejectValue("password", "NotBlank", "패스워드를 8글자 이상 입력하세요");
        }
        if (!memberForm.getPassword().equals(memberForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "MissMatch", "입력하신 패스워드가 다릅니다");
        }
        if (memberService.findByEmail(memberForm.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "AlreadyExist", "사용중인 이메일입니다");
        }
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        memberService.create(memberForm);
        return "redirect:/";
    }

    @GetMapping("/password")
    public String getPassword(@ModelAttribute("password") PasswordForm passwordForm) {
        return "password";
    }

    @PostMapping("/password")
    public String postPassword(@Valid @ModelAttribute("password") PasswordForm passwordForm,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal MemberUser userDetails) {

        if (!memberService.checkPassword(userDetails.getId(), passwordForm.getOld())) {
            bindingResult.rejectValue("old", "MissMatch", "비밀번호가 잘못 되었습니다");
        }
        if (!passwordForm.getPassword().equals(passwordForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "MissMatch", "비밀번호가 잘못 되었습니다");
        }
        if (bindingResult.hasErrors()) {
            return "/password";
        }

        memberService.updatePassword(userDetails.getId(), passwordForm.getPassword());
        return "redirect:/";
    }
}
