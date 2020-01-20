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
import com.example.postgretest.service.EmailSenderService;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import org.springframework.web.client.RestTemplate;

import static com.example.postgretest.util.Status.*;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import org.apache.tomcat.util.http.fileupload.IOUtils;


public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
    private EmailSenderService emailSenderService;
    private UserRepository userRepository;
	private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

	protected JWTLoginFilter(String url, AuthenticationManager authManager, 
		UserRepository userRepository, EmailSenderService emailSender){
		super(new AntPathRequestMatcher(url));
		super.setAuthenticationManager(authManager);
		this.userRepository = userRepository;
		this.emailSenderService = emailSender;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
                
                String s = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                
                System.out.println("Corpo da requisicao: "  + s + "\n\n\nFim" );
                
                
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

		if(failed instanceof org.springframework.security.authentication.BadCredentialsException){
			System.out.println("errou a senha");
		    String result;
		    String email = (String) request.getAttribute("username");

		    Usuario user = userRepository.findByEmail(email).get(0);
	        user.addTentativaErrada();

	        userRepository.save(user);

	        if(user.getTentativaErrada() >= MAX_NUM_TENTATIVAS){
	            //enviar email com instrucoes para usuario!.
	            emailSenderService.sendDesbloqueioToken(user);
	            result = ME10_1;
	        }else{
	        	result = ME09;
	        }

	        System.out.println(result);

		    messages[1] = result;
		}

		response.addHeader("msg", messages[1]);
		failureHandler.onAuthenticationFailure(request, response, failed);
	}
}