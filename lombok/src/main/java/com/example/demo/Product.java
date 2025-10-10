package com.example.demo;

import lombok.*;

//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@Builder
@EqualsAndHashCode(of = {"name", "price"})
public class Product {
    private String name;
    private String description;
    private int price;
}
