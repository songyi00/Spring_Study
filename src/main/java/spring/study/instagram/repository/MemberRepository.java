package spring.study.instagram.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import spring.study.instagram.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Optional<Member> findOne(Long id) {
        return Optional.ofNullable(em.find(Member.class,id));
    }

    // username 기준으로 User 정보 가져옴
    @EntityGraph(attributePaths = "authorities") // Eager 조회로 authorities 정보를 같이 가져오게 됨
    public Optional<Member> findOneWithAuthoritiesByUsername(String username){
        List<Member> result = em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", username)
                .getResultList();
        if (result.isEmpty()){
            return Optional.empty();
        }
        else{
            return Optional.ofNullable(result.get(0));
        }
    }
}
