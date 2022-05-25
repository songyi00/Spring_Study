package spring.study.instagram.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.study.instagram.domain.Authority;
import spring.study.instagram.domain.Member;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(true)
    void 회원저장 () {
        //given
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .dateJoined(LocalDateTime.now())
                .email("ksl2950@naver.com")
                .lastLogin(LocalDateTime.now())
                .name("김송이")
                .username("song2")
                .authorities(Collections.singleton(authority))
                .activated(true)
                .password("1234")
                .build();

        //when
        memberRepository.save(member);
        Member member1 = memberRepository.findOne(member.getId()).orElse(null);

        //then
        assertEquals(member.getUsername(),member1.getUsername());
    }

}