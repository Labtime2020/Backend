/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.postgretest.Controller.Resposta;
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

import org.springframework.web.bind.annotation.RequestBody;
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

    	List<Usuario> users = userRepository.findByEmail(usuario.email);

    	if(users.size() > 0){
    		return new Resposta(1, "Usuario com mesmo email ja cadastrado");
    	}else{
    		userRepository.save(new Usuario(123, usuario.nome, usuario.email, usuario.sobrenome, usuario.password, usuario.isAdmin, 1));
    		
    		return new Resposta(0, "Usuario criado com sucesso");
    	}
    }

    @GetMapping("/buscarusuarios")
    public List<UsuarioUI> buscarusuarios(){
    	System.out.println("Buscando todos os usuarios");

    	List<Usuario> users = userRepository.findAll();
    	List<UsuarioUI> usuarios = new ArrayList<>();

    	for(Usuario user: users){
    		usuarios.add(new UsuarioUI(user.getId(),user.getEmail(), user.getNome(), user.getSobrenome(),
    			user.getIsAdmin(), user.getPassword(), user.getStatus()));
    	}

    	return usuarios;
    }
}
