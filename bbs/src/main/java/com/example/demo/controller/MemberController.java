package com.example.demo.controller;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.MemberForm;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/list")
    public String getMemberList(@PageableDefault(page = 0, size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<MemberDto> page = memberService.findAll(pageable);
        model.addAttribute("page", page);
        return "member-list";
    }

    @GetMapping("/edit")
    public String getMemberEdit(@ModelAttribute("member") MemberForm memberForm) {
        MemberDto memberDto = memberService.findById(memberForm.getId());
        memberForm.setId(memberDto.getId());
        memberForm.setName(memberDto.getName());
        memberForm.setEmail(memberDto.getEmail());
        return "member-edit";
    }

    @PostMapping("/edit")
    public String postMemberEdit(@Valid @ModelAttribute("member") MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/member-edit";
        }
        memberService.patch(memberForm);
        return "redirect:/member/list";
    }

    @GetMapping("/delete")
    public String getMemberDelete(@RequestParam("id") Long id) {
        memberService.deleteById(id);
        return "redirect:/member/list";
    }
}
