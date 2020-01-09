/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.example.postgretest.Controller.Resposta;
import com.example.postgretest.util.Status;
import static com.example.postgretest.util.Status.*;
/**
 *
 * @author labtime
 */
@RestController

public class UserController {
    @Autowired 
    private UserRepository userRepository;
    private Usuario a;
    private Optional <Usuario> b;
    @PostMapping(path="/addUser")
    //public @ResponseBody String insertUser(@RequestParam String nome, @RequestParam String email, @RequestParam String sobrenome, @RequestParam String password){
    public @ResponseBody Resposta insertData(@RequestBody UsuarioUI user){
        if(userRepository.findByEmail(user.getEmail()).isEmpty()){
            Usuario t = new Usuario(user.getId(), user.getNome(), user.getEmail(), user.getSobrenome(), user.getPassword(), false, 1);
            t.setRegisterDate(new Date());/*falta converter para a data atual*/
            userRepository.save(t);
            return new Resposta(OK, "Usuario adicionado");
        }
        else
            return new Resposta(USERJAEXISTE,"ME04_2 - Usuario com email " + user.getEmail() + " ja existe no sistema");
    }
    @PostMapping(path="/updateUser")
    public @ResponseBody Resposta updateData(@RequestBody UsuarioUI user){
        System.out.println(user.getEmail());
        
       b = userRepository.findById(user.id);
        
        if( b.isEmpty() )
            return new Resposta(SEMUSER, "Nao foi encontrado usuario com este id!");
        else if( userRepository.findByEmail(user.email).isEmpty() == false ) //ja existe user com email fornecido, abortar
            return new Resposta(USERJAEXISTE, "ME04_2 - Usuario com email " + user.getEmail() + " ja existe no sistema");
        a.setEmail(user.getEmail());
        a.setNome(user.getNome());
        a.setPassword(user.getPassword());
        a.setSobrenome(user.getSobrenome());
        a.setStatus(user.getStatus());
        System.out.println(a.getId());
        userRepository.save(a);
        return new Resposta(OK, "User information updated");
    }
    @PostMapping(path="/addUserAsAdmin")
    public @ResponseBody Resposta addAdmin(@RequestBody UsuarioUI user ){
        
        b = userRepository.findById(user.id);
        
        if( b.isEmpty() )
            return new Resposta(SEMUSER, "Nao foi encontrado usuario com este id!");
        
        a = userRepository.findByEmail(user.email).get(0);
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
            return new Resposta(ERRO, "User not found");
        }
        
        a = userRepository.findByEmail(user.email).get(0);
        a.setIsAdmin(false);
        a.setAdminEndDate(new Date());
        userRepository.save(a);
        
        return new Resposta(OK, "User is no longer an admin!");
    }  
}
