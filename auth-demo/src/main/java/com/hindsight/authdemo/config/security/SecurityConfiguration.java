package com.hindsight.authdemo.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable() // rest api이므로 비인증시 로그인폼 화면으로 안가게설정
            .csrf().disable() // rest api라 필요없음
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//jwt 로 인증해 세션필요없어 생성 x
            .and()
                .authorizeRequests() // 하위 리퀘스트들에 대한 권한 체크
                    .antMatchers("/*/auth/**", "/h2-console/**").permitAll() //가입, 인증은 모두가능
                    // "/*/signin", "/*/signin/**", "/*/social/signup", "/*/signup/**",
                    .antMatchers(HttpMethod.GET, "/exception/**").permitAll() // 특정 api로의 get 요청 모두 허용
                    .anyRequest().hasRole("USER")// 그외 요청들은 모두 인증된 회원만 가능
            .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
            .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
                //jwt token 필터를 id/password 인증 필터 전에 넣는
    }

    @Override //swagger 리소스들은 모두 허용
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html",
                "/webjars/**", "/swagger/**", "/h2-console/**");
    }
}
