# XML 지원
- Accept : application/xml
- implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml'

# RESTful API 호출
- WebClient : 비동기/리액티브 필요, 직접 build.gradle에 추가, Spring Reactive Web (WebFlux)이 필요
- RestClient : 일반적인 REST 호출, 직접 build.gradle에 추가, Spring Web MVC 필요
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webclient'
    implementation 'org.springframework.boot:spring-boot-starter-restclient'
}
```

# RestTemplate
- @Bean RestTemplate
- restTemplate.postForEntity, getForEntity, put, patchForObject, delete
- Spring Boot Starter WebFlux
- @Bean WebClient
- webClient.get(), post()

# 그래들 테스트 후 결과 확인 (gradle test)
- 명령프롬프트 : echo %ERRORLEVEL%
- 파웨쉘 : Write-Host "Exit Code: $LASTEXITCODE"
- 리눅스/맥OS : echo $?