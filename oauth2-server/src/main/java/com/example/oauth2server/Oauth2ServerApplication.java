package com.example.oauth2server;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

@EnableAuthorizationServer
@SpringBootApplication
public class Oauth2ServerApplication {

// Token을 DB로 관리
//	@Bean
//	public TokenStore jdbcTokenStore(DataSource dataSource) {
//		return new JdbcTokenStore(dataSource);
//	}

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class, args);
    }

}

@Configuration
class OAuth2Configuration {

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        return new JwtAccessTokenConverter();
    }

    @Bean
    @Primary
    public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
        return new JdbcClientDetailsService(dataSource);
    }

}

@Configuration
class JwtOAuth2AuthorizationServerConfiguration extends OAuth2AuthorizationServerConfiguration {

    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final ClientDetailsService clientDetailsService;

    public JwtOAuth2AuthorizationServerConfiguration(BaseClientDetails details,
                                                     AuthenticationManager authenticationManager,
                                                     ObjectProvider<TokenStore> tokenStoreProvider,
                                                     AuthorizationServerProperties properties,
                                                     JwtAccessTokenConverter jwtAccessTokenConverter,
                                                     ClientDetailsService clientDetailsService) {
        super(details, authenticationManager, tokenStoreProvider, properties);
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints.accessTokenConverter(jwtAccessTokenConverter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
        super.configure(clients);
    }

    /*
    클라이언트 정보를 직접 기술하려면 이 부분의 주석을 풀면 된다.
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // 클라이언트 아이디
                .withClient("my_client_id")
                // 클라이언트 시크릿
                .secret("my_client_secret")
                // 엑세스 토큰 발급 가능한 인증 타입
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                // 클라이언트에 부여된 권한
                .authorities("ROLE_MY_CLIENT")
                // 접근할 수 있는 범위 제한
                .scopes("read","write")
                // 발급된 엑세스 토큰의 시간(단위:초)
                // default value: 60 * 60 * 12
                .accessTokenValiditySeconds(60 * 60 * 4)
                // 발급된 리프레시 토큰의 시간(단위:초)
                // default value: 60 * 60 * 24 * 30
                .refreshTokenValiditySeconds(60*60*24*120)
                // 클라이언트를 필요에 따라 계속 추가할 수 있음
                .and()
                .withClient("your_client_id")
                .secret("your_client_secret")
                .authorizedGrantTypes("authorization_code", "password")
                .scopes("read");

        super.configure(clients);
    }
    */
}


