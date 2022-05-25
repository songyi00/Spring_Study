package spring.study.instagram.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Authority {

    @Id @Column(name = "authority_name", length = 50)
    private String authorityName;

}
