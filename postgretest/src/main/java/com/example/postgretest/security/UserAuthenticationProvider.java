package com.example.postgretest.security;

import com.example.postgretest.repository.UserRepository;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.example.postgretest.util.Status.*;

public class UserAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	UserRepository repository;

	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	System.out.println("autenticando aqui!!\n");
    	UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

    	String s = token.getName();
    	
    	List<com.example.postgretest.model.Usuario> lists = repository.findByEmail(s);

    	if(lists.size() == 0){
    		throw new BadCredentialsException(ME09);
    	}

        com.example.postgretest.model.Usuario user = lists.get(0);
        
        if(user.getStatus() == INATIVO){
        	throw new BadCredentialsException(ME06);
        }

        String password = user.getPassword();

        boolean passwordsMatch = token.getCredentials().toString() == password;

        if(!passwordsMatch) {
            throw new BadCredentialsException("Invalid username/password");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, password, 
        	AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"));

        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}