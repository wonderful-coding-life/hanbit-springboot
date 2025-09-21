package com.example.demo.config;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
    @Bean
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
}
