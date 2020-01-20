/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.Controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.example.postgretest.Controller.Resposta;
import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.UsuarioUI;
import com.example.postgretest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletResponse;

import com.example.postgretest.service.UserService;
import com.example.postgretest.storage.FileSystemStorageService;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author labtime
 */
@RestController
@CrossOrigin(origins="http://localhost:9090", allowedHeaders = "*")
public class UserController {
    private final FileSystemStorageService storageService;
    @Autowired 
    private UserRepository userRepository;
    private Usuario a;
    private Optional <Usuario> b;
    @Autowired
    UserService userService;
   
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
    
    @PostMapping(path="/updateMyData")
    public @ResponseBody Resposta updateMyData(Authentication auth, MultipartFile file, @RequestParam("usuario") String usuarioString,
        HttpServletResponse response) throws JsonProcessingException{
        return userService.updateMyData(auth, file, usuarioString, response);
    }
    
    @PostMapping(path="/updateUser")
    public @ResponseBody Resposta updateData(@RequestParam("file") MultipartFile file, 
        Authentication auth, @RequestParam("usuario") String usuarioString)
            throws JsonProcessingException {
        return userService.updateData(file, auth, usuarioString);
    }
   

    @PostMapping(path="/addUserAsAdmin")
    public @ResponseBody Resposta addAdmin(Authentication auth, @RequestBody UsuarioUI user ){
        return userService.addAdmin(auth, user);
    }
    
    @PostMapping(path="/removeUserAsAdmin")
    public @ResponseBody Resposta removeAdmin(Authentication auth, @RequestBody UsuarioUI user){
        return userService.removeAdmin(auth, user);
    }
    

    @PostMapping(path="/updateUserStatus")
    public @ResponseBody Resposta updateUserStatus(Authentication auth, @RequestBody UsuarioUI user){
        return userService.updateUserStatus(auth, user);
    }
   
}
