package com.example.demo.model;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
public class MemberUser implements UserDetails {
    // User Details 디폴트 구현 getUsername(), getPassword(), getAuthorities()
    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;

    private String name;
    // article/add, article/edit에서 사용자를 확인하기 위함
    // username(email)이 아이디이므로 이것을 사용해도 되지만 향후 아이디 체계가 바뀔 수 있으므로 확실하게 하기 위해 id를 사용
    private Long id;

    public MemberUser(Member member, List<Authority> authorities) {
        this.username = member.getEmail();
        this.name = member.getName();
        this.password = member.getPassword();
        this.id = member.getId();
        this.authorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();
    }
}
