# XML 지원
- Accept : application/xml
- implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml'

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