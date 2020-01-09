/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.Controller;

import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PathVariable;

import com.example.postgretest.repository.UserRepository;

import java.util.Date;
import java.time.LocalDate;

import org.springframework.web.bind.annotation.RequestBody;

import static com.example.postgretest.util.Status.*;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 *
 * @author labtime
 */
@RestController

public class User1Controller {
    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    
    @PostMapping("/cadastrar")
    public Resposta cadastrar(@RequestBody UsuarioUI usuario){
    	System.out.println("Cadastrando Usuario");

    	List<Usuario> users = userRepository.findByEmail(usuario.email);
    	
    	if(users.size() > 0){
    		return new Resposta(ERRO, "Usuario com mesmo email ja cadastrado");
    	}else{
    		Usuario nuser = new Usuario(123, usuario.nome, usuario.email, usuario.sobrenome, usuario.password, usuario.isAdmin, ATIVO);

    		List<Usuario> test = userRepository.findAll();
	
			if(test.size() == 0){
				nuser.setIsAdmin(true);

				nuser.setAdminBeginDate(new Date());
			}	
			
			userRepository.save(nuser);

    		return new Resposta(OK, "Usuario criado com sucesso");
    	}
    }

    @GetMapping("/buscarusuarios")
    public List<UsuarioUI> buscarusuarios(){
    	System.out.println("Buscando todos os usuarios");

    	List<Usuario> users = userRepository.findAll();
    	List<UsuarioUI> usuarios = new ArrayList<>();

    	for(Usuario user: users){
    		usuarios.add(new UsuarioUI(user.getId(), user.getEmail(), user.getNome(), user.getSobrenome(),
    			user.getIsAdmin(), user.getPassword(), user.getStatus()));
    	}

    	return usuarios;
    }

    @GetMapping("/alterarsenha/{email}")
    public String alterarsenha(@PathVariable String email){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Alteracao de senha");
        msg.setText("O codigo de alteracao de senha eh 123456");

        javaMailSender.send(msg);

        return "Codigo enviado para o email " + email;
    }

    @GetMapping("/incrementar_erro/{email}/{credencial}")
    public String incrementar_erro(@PathVariable String email, @PathVariable String credencial){
        if(credencial != MCREDENCIAL){
            return "credencial invalida";
        }

        List<Usuario> user = userRepository.findByEmail(email);
        user.addTentativaErrada();

        userRepository.save(user);

        if(user.getTentativaErrada() >= 5){
            return "usuario bloqueado!";
        }

        return "usuario ainda possui tentativas";
    }
}
