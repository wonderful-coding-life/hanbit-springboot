package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원 생성 요청 DTO")
public class MemberRequest {
    @Schema(description = "회원 이름", example = "윤서준")
    private String name;
    @Schema(description = "회원 이메일", example = "SeojunYoon@hanbit.co.kr")
    private String email;
    @Schema(description = "회원 나이", example = "10")
    private Integer age;
}
