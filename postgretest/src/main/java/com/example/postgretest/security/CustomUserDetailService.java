package com.example.postgretest.security;

import java.util.ArrayList;

import com.example.postgretest.model.Aluno;
import com.example.postgretest.repository.UserRepository;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
	UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        com.example.postgretest.model.Usuario user = repository.findById((long)1).get();

        User userItem = new User(user.getEmail(), "{noop}" + user.getEmail(),true,true,true,true, AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"));

        return userItem;
    }
}