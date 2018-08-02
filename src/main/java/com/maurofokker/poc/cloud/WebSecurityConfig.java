package com.maurofokker.poc.cloud;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // client application would be considered a trusted client (dont have to be concerned about cross site request forgery)
                .csrf()
                    .disable()
                // any request need to be authenticated
                .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                // allow anyone to access token endpoint but will required authentication for any other request coming in for other paths (above config)
                .antMatchers("/oauth/token")
                    .permitAll()
                ;
    }
}
