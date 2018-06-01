package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login")    //<= custom login page  不理解
                .defaultSuccessUrl("/profile").and()
                .logout().logoutSuccessUrl("/login").and()
                .authorizeRequests()
                .antMatchers("/webjars/**","/login").permitAll()
                .anyRequest().authenticated();
    }
}
