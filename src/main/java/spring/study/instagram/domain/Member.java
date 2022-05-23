package spring.study.instagram.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 코드 안전을 위해
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String username; // 사용자 ID

    @JsonIgnore
    @Column(length = 100)
    private String password;

    @Column(length = 50)
    private String name; // 사용자 이름

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private boolean activated;

    @Column(name = "date_joined")
    private LocalDateTime dateJoined;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}
