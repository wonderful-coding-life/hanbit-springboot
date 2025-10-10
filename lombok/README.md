## @EqualsAndHashCode
* 기본은 주소를 사용하여 equals와 hashCode를 제공하지만 @EqualsAndHashCode를 사용하면 non-static 프로퍼티를 사용
* @EqualsAndHashCode(of = {"name", "price"})와 같이 특정 프로퍼티만 사용하도록 설정가능

## @Data
* Equivalent to @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode.
