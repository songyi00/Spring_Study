package spring.study.instagram.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import spring.study.instagram.jwt.JwtAccessDeniedHandler;
import spring.study.instagram.jwt.JwtAuthenticationEntryPoint;
import spring.study.instagram.jwt.JwtSecurityConfig;
import spring.study.instagram.jwt.TokenProvider;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity // web 보안을 활성화하겠다.
@EnableGlobalMethodSecurity(prePostEnabled = true) //PreAuthorize 어노테이션 사용 위해
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /*
    * WebSecurity
    * */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .antMatchers(
                        "/favicon.ico"
                        ,"/error"
                        ,"/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**"
                );
    }

    /*
     * HttpSecurity
     * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // csrf? 사용자의 의도치 않은 위조 요청
                // 이걸 막도록 보호하는 CSRF 를 disable
                // ( why? rest api 에서는 stateless 하기 때문에 서버에 인증정보를 저장하지 않고 헤더에 인증정보를 포함시켜 응담)
                // 따라서 csrf 불필요
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 x

                .and()

                .authorizeRequests()
                // 아래 3가지 api에 대해서는 토큰 없이도 호출 허용

                .antMatchers("/api/hello").permitAll()
                .antMatchers("/api/v1/authenticate").permitAll()
                .antMatchers("/api/v1/members").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
