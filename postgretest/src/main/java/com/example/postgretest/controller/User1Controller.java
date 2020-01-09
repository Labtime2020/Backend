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

import com.example.postgretest.model.DesbloqueioToken;
import com.example.postgretest.repository.DesbloqueioTokenRepository;
import com.example.postgretest.service.EmailSenderService;
import com.example.postgretest.Controller.Resposta;
import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.UsuarioUI;
import com.example.postgretest.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.time.LocalDate;

import static com.example.postgretest.util.Status.*;

/**
 *
 * @author labtime
 */
@RestController

public class User1Controller {
    @Autowired
    private DesbloqueioTokenRepository desbloqueioTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService javaMailSender;
    
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

    @GetMapping("/incrementar_erro/{email}")
    public String incrementar_erro(@PathVariable String email){
        Usuario user = userRepository.findByEmail(email).get(0);
        user.addTentativaErrada();

        userRepository.save(user);

        if(user.getTentativaErrada() >= MAX_NUM_TENTATIVAS){
            //enviar email com instrucoes para usuario!.
            emailSenderService.sendDesbloqueioToken(user);
            return ME10_1;
        }

        return ME09;
    }

    @GetMapping("/desbloquear")
    public String desbloquear(@RequestParam("token")String desbloqueioToken){
        DesbloqueioToken token = desbloqueioTokenRepository.findByDesbloqueioToken(desbloqueioToken);

        if(token != null){
            Usuario user = userRepository.findByEmail(token.getUsuario().getEmail()).get(0);
            
            if(user.getTentativaErrada() < MAX_NUM_TENTATIVAS){
                return ME11;
            }

            user.zerarTentativaErrada();
            userRepository.save(user);

            return MS02;
        }else{
            return ME12;
        }
    }
}