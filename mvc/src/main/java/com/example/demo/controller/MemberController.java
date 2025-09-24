package com.example.demo.controller;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/member/add")
    public String getMemberAdd() {
        return "member-add";
    }

    @PostMapping("/member/add")
    public String postMemberAdd(Member member) {
        memberRepository.save(member);
        return "redirect:/member/list";
    }

    @GetMapping("/member/list")
    public String getMemberList(Model model) {
        var members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "member-list";
    }

    @GetMapping("/member/edit")
    public String getMemberEdit(@RequestParam("id") Long id, Model model) {
        var member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("member", member);
        return "member-edit";
    }

    @PostMapping("/member/edit")
    public String postMemberEdit(Member member) {
        memberRepository.save(member);
        return "redirect:/member/list";
    }

    @GetMapping("/member/delete")
    public String getMemberEdit(@RequestParam("id") Long id) {
        memberRepository.deleteById(id);
        return "redirect:/member/list";
    }
}
