package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Controller
@Slf4j
public class HomeController {
    @GetMapping("/message")
    public String getMessage(Model model) {
        return "message";
    }

    @GetMapping("/message-param")
    public String getMessageKey(Model model) {
        // for message param
        model.addAttribute("name", "홍길동");
        model.addAttribute("email", "SeoJoonYun@hanbit.co.kr");
        // for message-key as param
        model.addAttribute("messageKey", "home.member.two");
        return "message-param";
    }

    @GetMapping("/model")
    public String getModel(Model model) {
        // put String
        model.addAttribute("name", "윤광철");
        model.addAttribute("email", "KwangcheolYun@hanbit.co.kr");

        // put Object
        var member = Member.builder().name("윤서준").email("SeojoonYun@hanbit.co.kr").age(10).build();
        model.addAttribute("member", member);

        return "model";
    }

    @GetMapping("/list")
    public String getList(Model model) {
        var members = List.of(Member.builder().name("윤서준").email("SeojunYoon@hanbit.co.kr").age(10).build(),
                Member.builder().name("윤광철").email("KwangcheolYoon@hanbit.co.kr").age(43).build(),
                Member.builder().name("공미영").email("MiyeongKong@hanbit.co.kr").age(23).build(),
                Member.builder().name("김도윤").email("DoyunKim@hanbit.co.kr").age(10).build());
        model.addAttribute("members", members);
        return "list";

        //return "list-with-common";
        //return "list-with-baselayout";
    }

    @GetMapping("/utility")
    public String getUtility(Model model) {
        //String formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime now = LocalDateTime.now();
        model.addAttribute("now", now);

        Instant instant = Instant.now();
        ZonedDateTime seoul = instant.atZone(ZoneId.of("Asia/Seoul"));
        model.addAttribute("seoul", seoul);
        ZonedDateTime paris = instant.atZone(ZoneId.of("Europe/Paris"));
        model.addAttribute("paris", paris);

        model.addAttribute("productPrice", 345620.5226);
        model.addAttribute("productCount", 3502340);

        return "utility";
    }

    @GetMapping("/condition")
    public String getCondition(Model model) {
        model.addAttribute("showHelloOne", true);
        model.addAttribute("showHelloTwo", false);

        return "condition";
    }

    @GetMapping("/link")
    public String getLink(Model model) {
        model.addAttribute("linkId", 3L);
        return "link";
    }

    @GetMapping("/link/path/{id}")
    public String getLinkPathvariable(@PathVariable Long id, Model model) {
        log.info("id={}", id);
        return "redirect:/link";
    }

    @GetMapping("/link/param")
    public String getLinkParam(@RequestParam("id") Long id, Model model) {
        log.info("id={}", id);
        return "redirect:/link";
    }
}
