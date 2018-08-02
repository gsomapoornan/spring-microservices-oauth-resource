package com.maurofokker.poc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.security.Principal;

@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
@SpringBootApplication
public class SpringMicroservicesOauthResourceApplication {

    @RequestMapping("/resource/endpoint")
    @PreAuthorize("hasRole('ADMIN')")
    public String endpoint(Principal principal) { // principal is injected by spring security
        return "Welcome " + principal.getName() + " to this resource protected by the resource server";
    }

    // allow to specify how to contact the token management services on the authorization server
    @Bean
    public RemoteTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        // to validate token received
        tokenServices.setCheckTokenEndpointUrl("http://localhost:9090/oauth/check_token");
        //
        tokenServices.setClientId("resource1");
        tokenServices.setClientSecret("secret");
        return tokenServices;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:hsql://127.0.0.1:9137/testdb");
        dataSource.setUsername("SA");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource()); // this way TokenStore will know how to connect to jdbc to look up the tokens
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringMicroservicesOauthResourceApplication.class, args);
    }
}
