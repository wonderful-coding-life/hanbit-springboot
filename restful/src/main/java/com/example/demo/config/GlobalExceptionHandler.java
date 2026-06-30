package com.example.demo.config;

import com.example.demo.exception.ArticleNotFoundException;
import com.example.demo.exception.ExceptionDetails;
import com.example.demo.exception.MemberNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionDetails> handleException(SQLException ex, HttpServletRequest request) {
        return ResponseEntity.status(500).body(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .path(getPath(request))
                .reason("데이터베이스에 문제가 발생했습니다.").build());
    }

    // 위 메서드와 동일 ExceptionDetails -> Map
    //@ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, Object>> handleExceptionUsingMap(SQLException ex, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>(); // LinkedHashMap은 put한 순서를 보장한다
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 500);
        body.put("path", getPath(request));
        body.put("reason", "데이터베이스에 문제가 발생했습니다.");
        return ResponseEntity.status(500).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handleException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(500).body(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(getPath(request))
                .reason("서버에 문제가 발생했습니다. " + ex.toString()).build());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleMemberNotFoundException(MemberNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(404).body(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .path(getPath(request))
                .reason("해당 아이디의 회원이 없습니다.").build());
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleArticleNotFoundException(ArticleNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(404).body(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .path(getPath(request))
                .reason("해당 아이디의 게시글이 없습니다.").build());
    }

    private String getPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String path = (query == null) ? uri : uri + "?" + query;
        return path;
    }
}
