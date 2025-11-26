package com.example.demo.controller;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("사용자 컨트롤러 테스트")
public class MemberControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자 생성 테스트")
    public void create() throws Exception {
        // prepare MemberRequest for creation
        MemberRequest memberRequest = MemberRequest.builder()
                .name("윤서준")
                .email("SeojunYoon@hanbit.co.kr")
                .age(10).build();

        // prepare json string from memberRequest
        String requestString = objectMapper.writeValueAsString(memberRequest);
//        String requestString = """
//                    {
//                        "name": "윤서준",
//                        "email": "SeojunYoon@hanbit.co.kr",
//                        "age": 10
//                    }
//                """;

        // prepare request builder
        var requestBuilder = MockMvcRequestBuilders.post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestString);

        // request to MockMvc and validate status code
        // MockMvcResultMatchers.status(), content(), jsonPath()...
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "name": "윤서준",
                            "email": "SeojunYoon@hanbit.co.kr"
                        }
                        """, JsonCompareMode.LENIENT))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        // parse response body as MemberResponse object
        MemberResponse memberResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), MemberResponse.class);

        // validate memberResponse
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getId()).isGreaterThan(0);
        assertThat(memberResponse.getName()).isEqualTo("윤서준");
    }
}
