package com.example.apiserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @PreAuthorize("#oauth2.hasScope('member.info.public')")
    @RequestMapping("/api/member")
    public MemberData member(@AuthenticationPrincipal OAuth2Authentication authentication) {

        String username = authentication.getUserAuthentication().getPrincipal().toString();
        Set<String> scopes = authentication.getOAuth2Request().getScope();
        log.info("Member's username = {}", username);
        log.info("Client scope info = {}", scopes);
        Member member = memberRepository.findByUsername(username);
        return MemberData.from(member, scopes);
    }

}
