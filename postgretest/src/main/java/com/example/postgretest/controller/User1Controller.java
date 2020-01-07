/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.Controller;


import java.util.List;
import com.example.postgretest.Resposta;
import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.UsuarioUI;
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
/**
 *
 * @author labtime
 */
@RestController

public class User1Controller {
    @Autowired 
    private UserRepository userRepository;
    
    @PostMapping("/cadastrar")
    public Resposta cadastrar(@RequestBody UsuarioUI usuario){
    	System.out.println("Cadastrando Usuario");

    	List<User> users = userRepository.findByEmail(usuario.email);

    	if(users.size() > 0){
    		return new Resposta(1, "Usuario com mesmo email ja cadastrado");
    	}else{
    		userRepository.save(new Usuario(123, usuario.nome, usuario.email, usuario.sobrenome, usuario.password, usuario.isAdmin));
    		
    		return new Resposta(0, "Usuario criado com sucesso");
    	}
    }
}
