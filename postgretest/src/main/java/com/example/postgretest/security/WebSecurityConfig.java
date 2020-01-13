package com.example.postgretest.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    private CustomUserDetailService userDetailsService;

	@Override
	protected void configure(HttpSecurity HttpSecurity) throws Exception {
		HttpSecurity.csrf().disable().authorizeRequests()
			.antMatchers("/addUser").permitAll()
                        .antMatchers("/updateUser").permitAll()
                        .antMatchers("/cadastrar").permitAll()
                        .antMatchers("/buscarusuarios").permitAll()
                        .antMatchers("/addUserAsAdmin").permitAll()
                        .antMatchers("/removeUserAsAdmin").permitAll()
                        .antMatchers("/alterarsenha/*").permitAll()
                        .antMatchers("/addNorma").permitAll()
                        .antMatchers("/incrementar_erro/*").permitAll()
                        .antMatchers("/desbloquear*").permitAll()
                        .antMatchers("/addNorma").permitAll()
                        .antMatchers("/updateNorma").permitAll()
                        .antMatchers("/uploadFile").permitAll()
                        .antMatchers("/").permitAll()
			.antMatchers(HttpMethod.POST, "/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JWTAuthenticationFilter(),
				UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
}