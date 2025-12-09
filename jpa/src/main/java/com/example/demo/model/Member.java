package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "member", indexes = {
        @Index(name = "idx_name_age", columnList = "name, age"),
        @Index(name = "idx_email", columnList = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name", nullable = false, length = 128)
    private String name;
    @Column(name="email", nullable = false, unique = true, length = 256)
    private String email;
    @Column(name="age", nullable = false, columnDefinition = "INTEGER DEFAULT 10")
    private Integer age;
    @Transient
    private String address;
//    @JsonIgnore 나중에 웹에서 가능
//    @ToString.Exclude
//    @OneToMany(mappedBy = "member")
//    private List<Article> articles;
}