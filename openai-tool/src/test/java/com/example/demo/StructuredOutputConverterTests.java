package com.example.demo;

import com.example.demo.model.ActorMovies;
import com.example.demo.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StructuredOutputConverterTests {
    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Test
    public void testClassLiteral() {
        Class<ActorMovies> clazz = ActorMovies.class;
        System.out.println("class name = " + clazz.getName());
        System.out.println("class package name = " + clazz.getPackageName());
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("field name = " + field.getName() + ", type = " + field.getType().getName());
        }
    }

    // converter.getFormat() - 결과 포맷에 대한 설명
    // converter.convert(String message) - LLM 출력을 사용하여 객체 생성
    @Test
    public void testConverter() {
        // Converter는 LLM의 응답을 자바 객체로 만들어 준다
        BeanOutputConverter<ActorMovies> beanOutputConverter = new BeanOutputConverter<>(ActorMovies.class);
        // 프롬프트에 넣을 힌트
        System.out.println("converter format=" + beanOutputConverter.getFormat());
        // LLM이 생성한 결과를 검증할 JSON 스키마 표준 (convert 호출시 LLM 출력을 검증한 후에 객체로 생성한다)
        System.out.println("converter jsonSchema=" + beanOutputConverter.getJsonSchema());

        // 프롬프트 구성 예
        String format = beanOutputConverter.getFormat();
        String actor = "브래드 피트";
        String message = MessageFormat.format("""
            {0}의 최신 출연작 5편의 영화를 알려 줘. 특히 2025년에 나온 영화가 있다면 포함해 줘.
            {1}
        """, actor, format);
        System.out.println("message = " + message);

        String result = openAiChatModel.call(message);
        System.out.println("result = " + result);

        ActorMovies actorMovies = beanOutputConverter.convert(result);
        assertThat(actorMovies).isNotNull();
        System.out.println("배우: " + actorMovies.getActor());
        for (Movie movie : actorMovies.getMovies()) {
            System.out.println("영화: " + movie);
        }
    }
}
