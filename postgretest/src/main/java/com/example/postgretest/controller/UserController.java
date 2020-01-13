/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.Controller;

import java.io.IOException;
import java.util.stream.Collectors;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;

import com.example.postgretest.model.DesbloqueioToken;
import com.example.postgretest.repository.DesbloqueioTokenRepository;
import com.example.postgretest.service.EmailSenderService;
import com.example.postgretest.Controller.Resposta;
import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.UsuarioUI;
import com.example.postgretest.model.Norma;
import com.example.postgretest.model.NormaUI;
import com.example.postgretest.repository.UserRepository;
import com.example.postgretest.repository.NormaRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.example.postgretest.storage.StorageFileNotFoundException;
import com.example.postgretest.storage.FileSystemStorageService;

import java.util.Date;
import java.time.LocalDate;

import static com.example.postgretest.util.Status.*;
/**
 *
 * @author labtime
 */
@RestController

public class UserController {
    private final FileSystemStorageService storageService;
    @Autowired 
    private UserRepository userRepository;
    private Usuario a;
    private Optional <Usuario> b;

    @Autowired
    public UserController(FileSystemStorageService storageService){
        this.storageService = storageService;
    }

    // @PostMapping(path="/addUser")
    // //public @ResponseBody String insertUser(@RequestParam String nome, @RequestParam String email, @RequestParam String sobrenome, @RequestParam String password){
    // public @ResponseBody Resposta insertData(@RequestBody UsuarioUI user){
    //     if(userRepository.findByEmail(user.getEmail()).isEmpty()){
    //         Usuario t = new Usuario(user.getId(), user.getNome(), user.getEmail(), user.getSobrenome(), user.getPassword(), false, 1);
    //         t.setRegisterDate(new Date());/*falta converter para a data atual*/
    //         userRepository.save(t);
    //         return new Resposta(OK, "Usuario adicionado");
    //     }
    //     else
    //         return new Resposta(USERJAEXISTE,"ME04_2 - Usuario com email " + user.getEmail() + " ja existe no sistema");
    // }

    @PostMapping(path="/alterarsenha")
    public @ResponseBody Resposta alterarsenha(Authentication auth, @RequestBody String novaSenha){
        System.out.println(auth.getName() + " eh o email");

        Usuario user = userRepository.findByEmail(auth.getName()).get(0);

        System.out.println(user.getPassword() + " == " + novaSenha);

        if(user.getPassword().equals(novaSenha)){
            return new Resposta(MESMASENHA, ME19);
        }

        user.setPassword(novaSenha);

        userRepository.save(user);

        return new Resposta(OK, "senha atualizada com sucesso");
    }

    @PostMapping(path="/updateUser")
    public @ResponseBody Resposta updateData(@RequestParam("file") MultipartFile file, @RequestParam("usuario") String usuarioString)
    throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        UsuarioUI user = mapper.readValue(usuarioString, UsuarioUI.class);

        System.out.println(user.getEmail());
        
        List <Usuario> c = userRepository.findByEmail(user.getEmail());
        
        if( c.isEmpty() ){
            return new Resposta(SEMUSER, "Nao foi encontrado usuario com este email!");
        }
        else if( c.get(0).getEmail().equals(user.getEmail()) == false){ //ja existe user com email fornecido, abortar
            System.out.println(c.get(0).getEmail() + " == " + user.getEmail());
            return new Resposta(USERJAEXISTE, "ME04_2 - Usuario com email " + user.getEmail() + " ja existe no sistema");
        }

        try{
            a = c.get(0);
            a.setId(user.getId());
            a.setEmail(user.getEmail());
            a.setNome(user.getNome());
            a.setPassword(user.getPassword());
            a.setSobrenome(user.getSobrenome());
            a.setStatus(user.getStatus());
            a.setAvatar("avatar_" + user.email + "." 
                        + storageService.getExtensao(file.getOriginalFilename()));

            userRepository.save(a);

            try{
                storageService.salvar(file, a.getAvatar());
            }catch(Exception ex){
                return new Resposta(ERRO, "Falha ao salvar avatar");
            }

            System.out.println(a.getId());
        }catch(Exception e){
            return new Resposta(ERRO, e.toString());
        }

        return new Resposta(OK, "User information updated");
    }

    @PostMapping(path="/addUserAsAdmin")
    public @ResponseBody Resposta addAdmin(@RequestBody UsuarioUI user ){
        
        b = userRepository.findById(user.id);
        
        if( b.isEmpty() )
            return new Resposta(SEMUSER, "Nao foi encontrado usuario com este id!");
        
        a = b.get();
        a.setIsAdmin(true);
        a.setAdminBeginDate(new Date());
        a.setAdminEndDate(null);
        userRepository.save(a);
      
        return new Resposta(OK, "Usuario agora eh um administrador!");
        
    }

    @PostMapping(path="/removeUserAsAdmin")
    public @ResponseBody Resposta removeAdmin(@RequestBody UsuarioUI user){
        b = userRepository.findById(user.id);
        
        try{
            if( b.isEmpty() )
                return new Resposta(SEMUSER, "Nao foi encontrado usuario com este id!");
            if( userRepository.findByIsAdmin(true).size() <= 1 )
                return new Resposta(UNADMIN, "Voce nao pode remover o unico administrador do sistema");
        }catch(Exception e){
            return new Resposta(ERRO, "Falha de comportamento!");
        }
        
        a = userRepository.findByEmail(user.email).get(0);
        a.setIsAdmin(false);
        a.setAdminEndDate(new Date());
        userRepository.save(a);
        
        return new Resposta(OK, "Usuario nao eh mais administrador");
    }  
}
