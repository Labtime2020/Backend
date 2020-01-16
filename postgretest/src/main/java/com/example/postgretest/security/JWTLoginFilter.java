package com.example.postgretest.security;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.postgretest.repository.UserRepository;
import com.example.postgretest.model.Usuario;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import org.springframework.web.client.RestTemplate;

import static com.example.postgretest.util.Status.*;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	@Autowired
    private UserRepository userRepository;
	private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

	protected JWTLoginFilter(String url, AuthenticationManager authManager){
		super(new AntPathRequestMatcher(url));
		super.setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		AccountCredentials credentials = new ObjectMapper()
			.readValue(request.getInputStream(), AccountCredentials.class);

		if(userRepository == null){
			System.out.println("nulo!");
		}

		System.out.println(credentials.getUsername());
		System.out.println(credentials.getPassword());
		request.setAttribute("username", credentials.getUsername());

		Authentication auth = getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
						credentials.getUsername(), 
						credentials.getPassword(), 
						Collections.emptyList()
						)
				);

		return auth;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication auth)
		throws IOException, ServletException {
		TokenAuthenticationService.addAuthentication(response, auth.getName(), auth.getAuthorities());
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {

		System.out.println(failed.toString());
		String messages[] = failed.toString().split(": ");

		/*PrintWriter out = response.getWriter();

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print("{teste: 'teste'}");
		out.flush();
	*/

		if(failed instanceof org.springframework.security.authentication.BadCredentialsException){
			System.out.println("Mandando aqui!!");

			final String uri = API_ADDRESS + "/incrementar_erro/" + request.getAttribute("username");

		    RestTemplate restTemplate = new RestTemplate();
		    String result = restTemplate.getForObject(uri, String.class);

		    messages[1] = result;
		}

		response.sendError(401, messages[1]);
	//	failureHandler.onAuthenticationFailure(request, response, failed);
	}
}