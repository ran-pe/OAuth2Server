package com.example.apiserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
@SpringBootApplication
public class ApiServerApplication {

	@Bean
	public ResourceServerConfigurerAdapter resourceServerConfigurerAdapter() {
		return new ResourceServerConfigurerAdapter() {
			public void configure(HttpSecurity http) throws Exception{
				http.headers().frameOptions().disable();
				http.authorizeRequests()
						.antMatchers("/members", "/members/**").access("#oauth2.hasScope('read')")
						.anyRequest().authenticated();
			}
		};
	}

	@Bean
	public CommandLineRunner commandLineRunner(MemberRepository memberRepository) {
		return args -> {
			memberRepository.save(new Member("김철수", "chulsoo.kim", "test1"));
			memberRepository.save(new Member("이철수", "chulsoo.lee", "test2"));
			memberRepository.save(new Member("박철수", "chulsoo.park", "test3"));
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiServerApplication.class, args);
	}

}
