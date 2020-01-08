package com.example.postgretest.Controller;

import com.example.postgretest.model.Usuario;
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
import com.example.postgretest.model.UsuarioUI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.web.bind.annotation.RequestBody;

@RestController

public class SecurityController{
	@Autowired 
    private UserRepository userRepository;

	@GetMapping("/usuariologado")
    public UsuarioUI currentUserName(Authentication authentication) {
    	Usuario user = userRepository.findByEmail(authentication.getName()).get(0);

    	return new UsuarioUI(user.getId(), user.getName(), user.getEmail(), user.getSobrenome(), user.getIsAdmin(), user.getPassword());
    }
}