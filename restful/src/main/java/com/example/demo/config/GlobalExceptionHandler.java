package com.example.demo.config;

import com.example.demo.exception.ExceptionDetails;
import com.example.demo.exception.MemberNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionDetails> handleException(SQLException ex) {
        return ResponseEntity.status(500).body(ExceptionDetails.builder()
                .timestamp(new Date())
                .status(500)
                .reason("데이터베이스에 문제가 발생했습니다.").build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handleException(Exception ex) {
        return ResponseEntity.status(500).body(ExceptionDetails.builder()
                .timestamp(new Date())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .reason("서버에 문제가 발생했습니다.").build());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleNotFoundException(MemberNotFoundException ex, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String path = (query == null) ? uri : uri + "?" + query;
        return ResponseEntity.status(404).body(ExceptionDetails.builder()
                .timestamp(new Date())
                .status(404)
                .path(path)
                .reason("해당 아이디의 회원이 없습니다.").build());
    }
}
