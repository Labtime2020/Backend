package com.example.postgretest.Controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.postgretest.repository.UserRepository;
import java.util.Date;
import java.time.LocalDate;

import static com.example.postgretest.util.Status.*;

import com.example.postgretest.model.UsuarioInfoUI;
import com.example.postgretest.model.Usuario;

@RestController

public class SecurityController{
	@Autowired 
    private UserRepository userRepository;
        
    @PostMapping("/usuariologado")
    public UsuarioInfoUI currentUserName(Authentication authentication) {
    	System.out.println(authentication.getName() + " eh o token");
    	Usuario user = userRepository.findByEmail(authentication.getName()).get(0);

    	return user.toUsuarioInfoUI();
    }
}