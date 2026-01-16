package com.example.demo.config;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.HttpSessionIdChangedEvent;

import java.time.Instant;
import java.util.function.Supplier;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Slf4j
public class SecurityConfig {

    // Step 1 - 사용자 인증과 패스워드 인코더
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Member member = memberRepository.findByEmail(username).orElseThrow(new Supplier<UsernameNotFoundException>() {
                    @Override
                    public UsernameNotFoundException get() {
                        return new UsernameNotFoundException("User not found: " + username);
                    }
                });
                return User.withUsername(username)
                        .password(member.getPassword())
                        .authorities(member.getAuthority()).build();
            }
        };
    }

    // 손님(세션 생성) -> 로그인(세션 아이디 변경) -> 로그아웃(세션 삭제, 새로운 손님 세션 생성)
    // 서블릿 컨테이너에서 발생하는 세션 이벤트를 스프링 내부로 "브릿지(bridge)" 해주는 클래스
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @EventListener
    public void onSessionCreatedEvent(HttpSessionCreatedEvent event) {
        log.info("created session id {} at {}",
                event.getSession().getId(),
                Instant.ofEpochMilli(event.getSession().getCreationTime()));
    }

    @EventListener
    public void onSessionIdChangedEvent(HttpSessionIdChangedEvent event) {
        log.info("session id changed from {} to {}",
                event.getOldSessionId(),
                event.getNewSessionId());
    }

    @EventListener
    public void onSessionDestroyedEvent(HttpSessionDestroyedEvent event) {
        log.info("destroyed session id {}, and last accessed at {}",
                event.getSession().getId(),
                Instant.ofEpochMilli(event.getSession().getLastAccessedTime()));
    }

    // 기타 Session
    // 컨트롤러에서 HttpSession을 메서드를 통해 주입 받은 후 setAttribute, getAttribute로 값을 넣거나 가져올 수 있다.
    // 컨트롤러에서 @AuthenticationPrincipal UserDetails userDetails 이렇게 하면 세션에 있는 정보를 전달함 (SecurityContextHolder.getContext().getAuthentication().getPrincipal() 과 동일)
    // 만약 이름만 필요하다면 public String getHome(Principal principal) 와 같이 Principal을 애노테이션 없이 가져와서 principal.getName() 이렇게 사용할 수 있다.

    // Step 2 - 권한 인가
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/product").permitAll()
                        .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .formLogin(withDefaults())  // formLogin is NOT enabled by default when you define SecurityFilterChain manually
                .logout(withDefaults()); // logout IS enabled by default, even if you don't configure it
        return http.build();
    }

    // 여러개의 시큐리티 필터 체인을 등록할 수 있으며, @Order 애노테이션을 사용하여 필터 체인의 우선순위를 지정할 수 있다.
    // 각 필터 체인에는 securityMatcher() 메서드를 사용하여 특정 URL 패턴에만 적용되도록 설정할 수 있다.
    // 웹 체인과 API 체인이 동시에 존재하는 경우 서로 다른 보안 요구사항을 가질 수 있기 때문에 이러한 구성이 유용하다.
    // 특히 API 체인은 상태 비저장(stateless) 특성을 가지므로 세션 관리를 비활성화하고, 세션이 없으므로 CSRF 보호도 비활성화하는 것이 일반적이다.
    // 또한 웹 체인이 함께 있고 여기에 .formLogin으로 AuthenticationEntryPoint가 설정되어 있으면 인증되지 않은 요청에 대해 401 Unauthorized 응답을 반환하도록 설정이 필요하다.
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/members/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    // Step 3 - 로그인, 로그아웃 폼 커스텀
    //@Bean
    public SecurityFilterChain securityFilterChainLoginLogout(HttpSecurity http) throws Exception {
        // "/" redirects to "/home" and both are permitted by all requests
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/product").permitAll()
                        .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()) // permit all for /login
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()); // permit all for logout success url
        return http.build();
    }


    // Step 4 - Http Basic Authentication for RESTful API
    // 헤더이름: Authorization 헤더값: Basic <base64(username:password)>
    // SeojunYoon@hanbit.co.kr:password ==> U2VvanVuWW9vbkBoYW5iaXQuY28ua3I6cGFzc3dvcmQ=
    // 자바에서 만들기: Base64.getEncoder().encodeToString(("username:password").getBytes())
    //@Bean
    public SecurityFilterChain securityFilterChainHttpBasic(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/product").permitAll()
                        .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .logout(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }

    // Step 5
    // Session concurrency (HttpSessionEventPublisher 필요)
    //@Bean
    public SecurityFilterChain securityFilterChainConcurrent(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/product").permitAll()
                        .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .logout(withDefaults())
                .sessionManagement(session -> session
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(1)
                                // true: 2번째 로그인 "거부", false: 2번째 로그인 허용하고 기존 세션 만료
                                // 2번쨰 로그인 "거부"한 상태에서 첫번째 로그인한 컴퓨터에서 브라우저를 종료하거나 컴퓨터를 끄더라도 서버에서 세션은 여전히 유효하기 때문에 두번째 컴퓨터에서 로그인할 수 없다. 이 경우 필요에 따라 세션 타임 아웃 시간(server.servlet.session.timeout=20m)을 줄인다. 디폴트는 30분.
                                .maxSessionsPreventsLogin(true)
                                .expiredUrl("/login?expired")));
        return http.build();
    }

    // Step 6 - 스프링 시큐리티에서 무시해야 할 패턴을 등록한다.
    // 정적 리소스 또는 필요에 따라 h2-console과 같은 패턴을 무시하도록 설정한다.
    // 스프링에서는 이러한 패턴들도 시큐리티 필터체인을 구성할 때 permitAll 하는 방식을 권장한다.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().requestMatchers(
                        "/h2-console/**",
                        "/css/**",
                        "/js/**",
                        "/image/**");
            }
        };
    }
}
