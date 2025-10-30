package com.example.demo.config;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.function.Supplier;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    // Step 1 - 사용자 인증
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

    // Step 2 - 권한 인가
    //@Bean
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

    // Step 4 - 스프링 시큐리티에서 무시해야 할 패턴을 등록한다.
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

    // Step 5 - Http Basic Authentication for RESTful API
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

    // Step 6
    // Session concurrency
    //
    @Bean
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

    // Concurrency를 구현하려면 이것이 있어야 한다.
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // 기타 Session
    // 컨트롤러에서 HttpSession을 메서드를 통해 주입 받은 후 setAttribute, getAttribute로 값을 넣거나 가져올 수 있다.
    // 컨트롤러에서 @AuthenticationPrincipal UserDetails userDetails 이렇게 하면 세션에 있는 정보를 전달함 (SecurityContextHolder.getContext().getAuthentication().getPrincipal() 과 동일)
    // 만약 이름만 필요하다면 public String getHome(Principal principal) 와 같이 Principal을 애노테이션 없이 가져와서 principal.getName() 이렇게 사용할 수 있다.
}
