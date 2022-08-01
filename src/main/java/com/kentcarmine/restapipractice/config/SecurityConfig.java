package com.kentcarmine.restapipractice.config;

import com.kentcarmine.restapipractice.security.CustomPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    //needed for use with Spring Data JPA SPeL
//    @Bean
//    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
//        return new SecurityEvaluationContextExtension();
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return CustomPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable(); // TODO: For Dev only
//
//        http
//                .authorizeRequests(authorize -> {
//                    authorize
//                            .antMatchers("/api/v1/**").permitAll()
//                            .antMatchers("/h2-console/**").permitAll(); // TODO: FOR DEV ONLY!
//                })
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic();
////                .and()
////                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
//
//        // TODO: FOR DEV ONLY!
//        // H2 Console Config
//        http.headers().frameOptions().sameOrigin();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // TODO: For Dev only

        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/api/v1/**").permitAll()
                            .antMatchers("/h2-console/**").permitAll(); // TODO: FOR DEV ONLY!
                })
//                .authorizeRequests().anyRequest().authenticated().and()
                .httpBasic();

        // TODO: FOR DEV ONLY!
        // H2 Console Config
        http.headers().frameOptions().sameOrigin();
    }
}
