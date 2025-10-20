package com.example.demo.repository;

import com.example.demo.model.Member;
import com.example.demo.model.MemberStats;
import com.example.demo.model.MemberStatsNative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // All belows are same
    List<Member> findAllByName(String name);
    List<Member> findByName(String name);
    List<Member> findByNameIs(String name);
    List<Member> findByNameEquals(String name);

    // All belows are same
    List<Member> getByName(String name);
    List<Member> queryByName(String name);
    List<Member> readByName(String name);
    List<Member> searchByName(String name);

    // Generic sample
    // SELECT * FROM MEMBER WHERE name = '...' AND email = '...' OR age > ...
    List<Member> findByNameIsAndEmailIsOrAgeIsGreaterThan(String name, String email, Integer age);
    List<Member> queryByNameEqualsAndEmailIsOrAgeGreaterThan(String name, String email, Integer age);
    List<Member> searchByNameAndEmailOrAgeGreaterThan(String name, String email, Integer age);

    // 이름과 이메일을 AND 조건으로 사용자 조회
    List<Member> findByNameAndEmail(String name, String email);

    // 이름과 이메일을 OR 조건으로 사용자 조회
    List<Member> findByNameOrEmail(String name, String email);

    // 이름의 시작으로 사용자 조회
    List<Member> findByNameStartingWith(String name);

    // 이름의 마지막으로 사용자 조회
    List<Member> findByNameEndingWith(String name);

    // 이름을 포함하는 사용자 조회
    List<Member> findByNameContaining(String name);

    // 이름을 포함하는 사용자 조회로 LIKE 검색을 위한 와일드카드 %를 매개 변수에 포함
    // 예) findByNameLike("%윤%")
    List<Member> findByNameLike(String name);

    // 나이 정보가 존재하지 않는 사용자 조회
    List<Member> findByAgeIsNull();

    // 나이 정보가 존재하는 사용자 조회
    List<Member> findByAgeIsNotNull();

    // 매개변수로 전달된 나이로 사용자 조회
    List<Member> findByAgeIs(Integer age);

    // 매개변수로 전달된 나이보다 많은 사용자 조회
    List<Member> findByAgeGreaterThan(Integer age);
    List<Member> findByAgeAfter(Integer age);

    // 매개변수로 전달된 나이보다 같거나 많은 사용자 조회
    List<Member> findByAgeGreaterThanEqual(Integer age);

    // 매개변수로 전달된 나이보다 적은 사용자 조회
    List<Member> findByAgeLessThan(Integer age);
    List<Member> findByAgeBefore(Integer age);

    // 매개변수로 전달된 나이보다 같거나 적은 사용자 조회
    List<Member> findByAgeLessThanEqual(Integer age);

    // 매개변수로 전달된 나이를 포함하여 그 사이 나이의 사용자 조회
    List<Member> findByAgeBetween(Integer min, Integer max);

    // 이름 순으로 조회
    List<Member> findByOrderByNameAsc();

    // 이름의 역순으로 조회
    List<Member> findByOrderByNameDesc();

    // 이름 순으로 조회하고 이름이 같은 경우에는 나이의 역순으로 조회
    List<Member> findByOrderByNameAscAgeDesc();

    // 이름의 일부로 검색하고 그 결과는 이름 순으로 조회
    // 조건과 정렬 방법등이 함께 이름에 사용되면 이름이 길어져 가독성이 떨어진다
    List<Member> findByNameContainingOrderByNameAsc(String name);

    // 나이 순으로 정렬하고 나이가 가장 적은 한명을 조회
    Member findFirstByOrderByAgeAsc();
    Member findTopByOrderByAgeAsc();

    // 나이 순으로 정렬하고 나이가 가장 적은 두명을 조회
    List<Member> findFirst2ByOrderByAgeAsc();
    List<Member> findTop2ByOrderByAgeAsc();

    // 이름의 일부분으로 검색하고 Sort 객체의 정보를 기반으로 정렬
    //  Sort sort = Sort.by(Sort.Order.asc("name"), Sort.Order.desc("age"));
    List<Member> findByNameContaining(String name, Sort sort);

    // 이름의 일부분으로 검색하고 Pageable 객체의 정보를 기반으로 페이지 조회
    // Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "name")); pageNumber, pageSize
    Page<Member> findByNameContaining(String name, Pageable pageable);

    // 이메일을 사용하여 사용자 삭제
    //@Transactional
    int deleteByEmail(String email);

    // 이름을 사용하여 사용자 삭제
    //@Transactional
    int deleteByName(String name);

    @Query("SELECT m FROM Member m WHERE m.name = :name")
    List<Member> findMember(@Param("name") String name);

    @Query("SELECT m FROM Member m WHERE m.name = :name AND m.email = :email")
    List<Member> findMember(@Param("name") String name, @Param("email") String email);

    @Query("""
            SELECT m.name, m.email, COUNT(a.id) as count
                FROM Member m LEFT JOIN Article a ON a.member = m
                GROUP BY m ORDER BY count DESC
    """)
    List<Object[]> getMemberStatsObject();

    @Query("""
        SELECT new com.example.demo.model.MemberStats(m.name, m.email, COUNT(a.id) as count)
            FROM Member m LEFT JOIN Article a ON a.member = m
            GROUP BY m ORDER BY count DESC
    """)
    List<MemberStats> getMemberStats();

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.age = :age")
    int setMemberAge(Integer age);

    @Query(value="""
        SELECT m.name, m.email, count(a.id) AS count
            FROM member m LEFT JOIN article a ON m.id = a.member_id
            GROUP BY m.id ORDER BY count DESC
    """, nativeQuery = true)
    List<Object[]> getMemberStatsNativeObjects();

    @Query(value="""
        SELECT m.name, m.email, count(a.id) AS count
            FROM member m LEFT JOIN article a ON m.id = a.member_id
            GROUP BY m.id ORDER BY count DESC
    """, nativeQuery = true)
    List<MemberStatsNative> getMemberStatsNative();
}
