package com.example.postgretest.security;

import java.io.PrintWriter;

import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.example.postgretest.repository.UserRepository;
import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.UsuarioUI;

public class TokenAuthenticationService{
	@Autowired
	private CustomUserDetailService userDetailsService;

	static final long EXPIRATION_TIME = 1
	;
	static final String SECRET = "MySecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	public static void addAuthentication(HttpServletResponse response, String username, Collection<? extends GrantedAuthority>  auths) {
		String role = "";

		for(GrantedAuthority auth: auths){
			role = auth.getAuthority();
		}

		String JWT = Jwts.builder()
				.setSubject(username + "." + role)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		
		System.out.println(username + " foi passado no token");
		
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			// faz parse do token
			String ptoken = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody()
					.getSubject();


			if (ptoken != null && ptoken.lastIndexOf(".") != -1) {		
				String user = ptoken.substring(0, ptoken.lastIndexOf("."));
				String role = ptoken.substring(ptoken.lastIndexOf(".") + 1, ptoken.length());

				System.out.println(user);
				System.out.println(role);

				return new UsernamePasswordAuthenticationToken(user, null, 
					AuthorityUtils.createAuthorityList(role));
			}
		}
		return null;
	}
}