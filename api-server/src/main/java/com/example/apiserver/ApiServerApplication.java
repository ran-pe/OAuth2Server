package com.example.apiserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import java.util.Calendar;

@EnableResourceServer
@SpringBootApplication
public class ApiServerApplication {

    @Bean
    public ResourceServerConfigurerAdapter resourceServerConfigurerAdapter() {
        return new ResourceServerConfigurerAdapter() {
            public void configure(HttpSecurity http) throws Exception {
                http.headers().frameOptions().disable();
                http.authorizeRequests()
                        .antMatchers("/members", "/members/**").access("#oauth2.hasScope('member.info.public')")
                        .anyRequest().authenticated();
            }
        };
    }

    @Bean
    public CommandLineRunner commandLineRunner(MemberRepository memberRepository) {
        return args -> {
            memberRepository.save(new Member("김철수", "chulsoo.kim", "chulsoo.kim@email.com", "01012341234", "kim", Calendar.getInstance().getTime(), "test01"));
            memberRepository.save(new Member("이철수", "chulsoo.lee", "chulsoo.lee@email.com", "01056785678", "lee", Calendar.getInstance().getTime(), "test02"));
            memberRepository.save(new Member("박철수", "chulsoo.park", "chulsoo.park@email.com", "01090129012", "park", Calendar.getInstance().getTime(), "test03"));
            memberRepository.save(new Member("유저", "user", "user@email.com", "01034563456", "user", Calendar.getInstance().getTime(), "test04"));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiServerApplication.class, args);
    }

}
