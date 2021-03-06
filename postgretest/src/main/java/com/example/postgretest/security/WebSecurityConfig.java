package com.example.postgretest.security;

import com.example.postgretest.repository.UserRepository;
import com.example.postgretest.model.Usuario;
import com.example.postgretest.service.EmailSenderService;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
      @Autowired
      private UserRepository userRepository;

	@Autowired
      private CustomUserDetailService userDetailsService;

      @Autowired
      private EmailSenderService javaMailSender;

	@Override
	protected void configure(HttpSecurity HttpSecurity) throws Exception {
		HttpSecurity.csrf().disable().authorizeRequests()
                        
			.antMatchers("/addUser").permitAll()
                        .antMatchers("/updateUser").hasRole("ADMIN")
                        .antMatchers("/updateMyData").hasAnyRole("USER", "ADMIN")
                        .antMatchers("/cadastrar").permitAll()
                        .antMatchers("/buscarusuarios").permitAll()
                        .antMatchers("/addUserAsAdmin").permitAll()
                        .antMatchers("/removeUserAsAdmin").permitAll()
                        .antMatchers("/updateUserStatus").hasRole("ADMIN")
                        .antMatchers("/alterarsenha/*").hasAnyRole("ADMIN", "USER")
                        .antMatchers("/usuariologado/").hasAnyRole("ADMIN", "USER")
                        .antMatchers("/incrementar_erro/*").permitAll()
                        .antMatchers("/desbloquear*").permitAll()
                        .antMatchers("/addNorma").hasRole("ADMIN")
                        .antMatchers("/updateNorma").permitAll()
                        .antMatchers("/uploadFile").permitAll()
                        .antMatchers("/updateNormaStatus").permitAll()
                        .antMatchers("/obterArquivoNorma").permitAll()
                        .antMatchers("/visualizarNorma").permitAll()
                        .antMatchers("/").permitAll()
                        .antMatchers("/hey").permitAll()
                        .antMatchers("/recuperarsenha").permitAll()
                        .antMatchers("/buscarnormas").permitAll()
			.antMatchers(HttpMethod.POST, "/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), userRepository, javaMailSender),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JWTAuthenticationFilter(),
				UsernamePasswordAuthenticationFilter.class);
                                
                
        }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

}
