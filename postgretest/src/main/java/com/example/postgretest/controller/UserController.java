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

import javax.servlet.http.HttpServletResponse;

import com.example.postgretest.security.TokenAuthenticationService;
import com.example.postgretest.storage.StorageFileNotFoundException;
import com.example.postgretest.storage.FileSystemStorageService;

import java.util.Date;
import java.time.LocalDate;

import static com.example.postgretest.util.Status.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
    
    private boolean isAdmin( Authentication auth ) throws Exception{
        List<Usuario> users = userRepository.findByEmail(auth.getName());
        if( users.isEmpty() )
            throw new Exception("Nao existe usuario com este id");
        else{
            users.get(0).atualizarEntrada();
            userRepository.save(users.get(0));

            return (users.get(0).getIsAdmin() == true) ? true : false;
        }
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
    
    @PostMapping(path="/updateMyData")
    public @ResponseBody Resposta updateMyData(Authentication auth, MultipartFile file, @RequestParam("usuario") String usuarioString,
        HttpServletResponse response) throws JsonProcessingException{
        List<Usuario> usuarioLogado = userRepository.findByEmail(auth.getName());
        ObjectMapper mapper = new ObjectMapper();
        UsuarioUI user = mapper.readValue(usuarioString, UsuarioUI.class);
        
        if(usuarioLogado.isEmpty() == false){
            Usuario userAtual = usuarioLogado.get(0);
            userAtual.atualizarEntrada();

            List<Usuario> outros = userRepository.findByEmail(user.getEmail());
            if(outros.isEmpty() == false && outros.get(0).getId() != userAtual.getId() ){
                System.out.println("Update de email nao sera feito, pois ja existe usuario cadastrado!");
                return new Resposta(USERJAEXISTE, ME04_2);
            }
            /*
            else if( user.getPassword().equals(userAtual.getPassword())){
                return new Resposta(ERRO, ME19);
            }*/
            else{
                userAtual.setEmail(user.getEmail());
                userAtual.setPassword(user.getPassword());
                if( user.getNome()!=null && (!user.getNome().isBlank() || !user.getNome().isEmpty()) )
                    userAtual.setNome(user.getNome());
                if( user.getSobrenome() != null &&( !user.getSobrenome().isBlank() || !user.getSobrenome().isEmpty() ) )
                    userAtual.setSobrenome(user.getSobrenome());
                if(file != null)
                    userAtual.setAvatar("avatar_" + user.email + "." 
                            + storageService.getExtensao(file.getOriginalFilename()));
            }
            userRepository.save(userAtual);
            
            try{
                if(file != null)
                    storageService.salvar(file, userAtual.getAvatar());
            }catch(Exception ex){
                return new Resposta(ERRO, "Falha ao salvar avatar");
            }
            
            TokenAuthenticationService.addAuthentication(response, userAtual.getEmail(), auth.getAuthorities());

            System.out.println(userAtual.getId());
            return new Resposta(OK, "User information updated");
        }
        
        return new Resposta(SEMUSER, "Nada a fazer com este usuario!");
    }

    @PostMapping(path="/updateUser")
    public @ResponseBody Resposta updateData(@RequestParam("file") MultipartFile file, 
        Authentication auth, @RequestParam("usuario") String usuarioString)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UsuarioUI user = mapper.readValue(usuarioString, UsuarioUI.class);

        Usuario usuarioLogado = userRepository.findByEmail(auth.getName()).get(0);

        usuarioLogado.atualizarEntrada();
        userRepository.save(usuarioLogado);

        System.out.println(user.getEmail());
        
        Optional<Usuario> c = userRepository.findById(user.getId());
        
        if( c.isEmpty() ){
            return new Resposta(SEMUSER, "Nao foi encontrado usuario com este email!");
        }
        /*
        else if( c.get().getEmail().equals(user.getEmail()) == false){ //ja existe user com email fornecido, abortar
            System.out.println(c.get().getEmail() + " Este usuario ja existe no sistema ");
            return new Resposta(USERJAEXISTE, ME04_2);
        }*/
        try{
            a = c.get();
            a.setEmail(user.getEmail());
            if( user.getNome()!= null || !user.getNome().isEmpty() || !user.getNome().isBlank() )
                a.setNome(user.getNome());
            if( user.getSobrenome() != null &&( !user.getSobrenome().isBlank() || !user.getSobrenome().isEmpty() ) )
                a.setSobrenome(user.getSobrenome());
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
    public @ResponseBody Resposta addAdmin(Authentication auth, @RequestBody UsuarioUI user ){
        try{/*Checagem de seguranca, somente usuarios administradores podem adicionar outros users como admin*/
            if(isAdmin(auth) == false)
                return new Resposta(ERRO,ME_C_1);
        }
        catch(Exception e){
            e.getMessage();
        }
        
        List<Usuario> c = userRepository.findByEmail(user.getEmail());
        
        if( c.isEmpty() )
            return new Resposta(SEMUSER, "Nao foi encontrado usuario com este id!");
        
        Usuario usuariolog = userRepository.findByEmail(auth.getName()).get(0);

        usuariolog.atualizarEntrada();
        userRepository.save(usuariolog);

        a = c.get(0);
        a.setIsAdmin(true);
        a.setAdminBeginDate(new Date());
        a.setAdminEndDate(null);
        userRepository.save(a);
      
        return new Resposta(OK, "Usuario agora eh um administrador!");
    }

    @PostMapping(path="/removeUserAsAdmin")
    public @ResponseBody Resposta removeAdmin(Authentication auth, @RequestBody UsuarioUI user){
        try{/*Checagem de seguranca, somente usuarios administradores podem adicionar outros users como admin*/
            if(isAdmin(auth) == false)
                return new Resposta(ERRO,ME_C_1);
        }
        catch(Exception e){
            e.getMessage();
        }
        
        b = userRepository.findById(user.id);
        
        try{
            if( b.isEmpty() )
                return new Resposta(SEMUSER, "Nao foi encontrado usuario com este id!");
            if( userRepository.findByIsAdmin(true).size() <= 1 )
                return new Resposta(UNADMIN, "Voce nao pode remover o unico administrador do sistema");
        }catch(Exception e){
            return new Resposta(ERRO, "Falha de comportamento!");
        }
        
        Usuario userlog = userRepository.findByEmail(auth.getName()).get(0);
        
        userlog.atualizarEntrada();
        userRepository.save(userlog);

        a = userRepository.findByEmail(user.email).get(0);
        a.setIsAdmin(false);
        a.setAdminEndDate(new Date());
        a.atualizarEntrada();
        userRepository.save(a);
        
        return new Resposta(OK, "Usuario nao eh mais administrador");
    }

    @PostMapping(path="/updateUserStatus")
    public @ResponseBody Resposta updateUserStatus(Authentication auth, @RequestBody UsuarioUI user){
        
        if( user.getEmail() == null || user.getStatus() > 1 || user.getStatus() < 0 ){
                return new Resposta(ERRO, JSONINVALIDO);
        }
        List<Usuario> userChk = userRepository.findByEmail(user.getEmail());
        if( userChk.isEmpty() ){
            return new Resposta(SEMUSER, ME_C_2);
        }
        else{
            Usuario usuariolog = userRepository.findByEmail(auth.getName()).get(0);

            usuariolog.atualizarEntrada();
            userRepository.save(usuariolog);

            Usuario usuario = userChk.get(0);
            if( user.getStatus() == 0 ){
                if( auth.getName() == user.getEmail() ){//usuario esta querendo se auto desativar
                    return new Resposta(ERRO, ME22);
                }
                else if( userRepository.findByIsAdmin(true).size() == 1
                        && usuario.getIsAdmin() == true ){
                    return new Resposta(UNADMIN, ME05);
                }
                else if( usuario.getIsAdmin() == true ){
                    usuario.setIsAdmin(false);
                    usuario.setAdminEndDate(new Date());
                }
                else
                    usuario.setStatus(0);
            }
            else{/*ativar usuario*/
                usuario.setStatus(1);
            }
            userRepository.save(usuario);
            
            return new Resposta(OK, MS01);
        }
    }
}
