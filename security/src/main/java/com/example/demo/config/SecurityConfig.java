package com.example.demo.config;

import com.example.demo.model.Authority;
import com.example.demo.model.Member;
import com.example.demo.model.MemberUserDetails;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//@EnableWebSecurity(debug = true)
public class SecurityConfig {

    //@Bean
    public SecurityFilterChain securityFilterChainDefault(HttpSecurity http) throws Exception {
        http
                .csrf(withDefaults()) // CSRF protection is enabled by default
                // authorizeHttpRequests default:
                // - If you define your own SecurityFilterChain → all requests require authentication
                // - If you rely on Spring Boot auto-config → some static resources (/css, /js, /images, /error) are permitAll
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .formLogin(withDefaults()) // formLogin is NOT enabled by default when you define SecurityFilterChain manually
                .logout(withDefaults()) // logout IS enabled by default, even if you don't configure it
                .httpBasic(withDefaults()); // HTTP Basic is NOT enabled by default
        ;
        return http.build();
    }

    //@Bean
    public SecurityFilterChain securityFilterChainCors(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(withDefaults()) // default enabled
                //.cors(cors -> cors.disable())
                //.cors(withDefaults()) // search CorsConfigurationSource bean
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .formLogin(withDefaults())
                .logout(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }

    //@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.addAllowedOrigin("*");
        //configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedOrigins(Arrays.asList("https://hanbit.co.kr", "https://www.hanbit.co.kr"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //@Bean
    public SecurityFilterChain securityFilterChainRememberMe(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        http
                .csrf(withDefaults()) // default enabled
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .rememberMe(withDefaults()) // TokenBasedRememberMeServices is applied
                //.rememberMe(remember -> remember.rememberMeServices(rememberMeServices))
                .formLogin(withDefaults())
                .logout(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        return new PersistentTokenBasedRememberMeServices("myRememberMeKey", userDetailsService, tokenRepository);
    }

    @Bean
    PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }

    //@Bean
    public SecurityFilterChain securityFilterChainLoginLogout(HttpSecurity http) throws Exception {
        // "/" redirects to "/home" and both are permitted by all requests
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated())
                .rememberMe(withDefaults())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()) // permit all for /login
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .permitAll()); // permit all for logout success url
        return http.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .formLogin(withDefaults())  // formLogin is NOT enabled by default when you define SecurityFilterChain manually
                .logout(withDefaults()); // logout IS enabled by default, even if you don't configure it
        return http.build();
    }

    //@Bean
    public UserDetailsService userDetailsServiceInMemory() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}password") // {noop} can be used for non-encrypted password only when InMemoryUserDetailsManager and no PasswordEncoder bean.
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}password")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //@Bean
    UserDetailsManager userDetailsManagerJdbc(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public UserDetailsService userDetailsServiceCustom(MemberRepository memberRepository, AuthorityRepository authorityRepository) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Member member = memberRepository.findByEmail(username).orElseThrow();
                List<Authority> authorities = authorityRepository.findByMember(member);
                return new MemberUserDetails(member, authorities);
            }
        };
    }

    // 스프링 시큐리티에서 무시해야 할 패턴을 등록한다.
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

    // Member로 Authority를 @OneToMany 관계 매핑을 하여 Authorities에 접근한다면
    // (1) @OneToMany의 FetchType.EAGER를 사용하거나 - 권장하지 않음
    // (2) FilterRegistrationBean을 만들어서 필터에서도 영속성 컨텍스트를 사용할 수 있도록 해야 한다.
    // JPA 영속성 컨텍스트를 필터에서도 사용할 수 있도록 OpenEntityManagerInViewFilter를 맨 앞 필터로 등록한다.
    // @Bean
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> filterRegistrationBeanForOpenEntityManagerInViewFilter() {
        FilterRegistrationBean<OpenEntityManagerInViewFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new OpenEntityManagerInViewFilter());
        filterFilterRegistrationBean.setOrder(Integer.MIN_VALUE);
        return filterFilterRegistrationBean;
    }
}
