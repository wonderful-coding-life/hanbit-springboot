package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberForm {
    private Long id;
    @NotBlank(message = "이름을 입력하세요")
    private String name;
    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일 형식이 잘못 되었습니다")
    private String email;
    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String password;
    @NotBlank(message = "비밀번호 확인을 입력하세요")
    private String passwordConfirm;
}