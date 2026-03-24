package com.example.demo.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * implements ErrorController를 구현하는 것은 Spring Boot에게 이 컨트롤러가 에러 처리용이다라고 알려주기 위한 마커(marker) 역할
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = statusObj != null ? Integer.parseInt(statusObj.toString()) : 500;

        HttpStatus status = HttpStatus.valueOf(statusCode);

        model.addAttribute("status", statusCode);
        model.addAttribute("error", status.getReasonPhrase());
        model.addAttribute("message", "요청 처리 중 오류가 발생했습니다.");
        model.addAttribute("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

        return "error/custom-error";
    }
}
