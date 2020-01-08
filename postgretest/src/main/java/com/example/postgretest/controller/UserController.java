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
/**
 *
 * @author labtime
 */
@RestController

public class UserController {
    @Autowired 
    private UserRepository userRepository;
    private Usuario a;
    @GetMapping(path="/addUser")
    //public @ResponseBody String insertUser(@RequestParam String nome, @RequestParam String email, @RequestParam String sobrenome, @RequestParam String password){
    public @ResponseBody String insertData(){
        Usuario t = new Usuario(1, "andre", "andre@gmail.com", "boy", "andre123", true, 1);
        t.setRegisterDate(new Date());/*falta converter para a data atual*/
        userRepository.save(t);
        return "User has been added to the database";
    }
    @PostMapping(path="/updateUser")
    public @ResponseBody String updateData(@RequestBody UsuarioUI user){
        System.out.println(user.getEmail());
        if( userRepository.findByEmail(user.email).isEmpty() )
            return "User not found";
        a =  userRepository.findByEmail(user.email).get(0);
        
        a.setEmail(user.getEmail());
        a.setNome(user.getNome());
        a.setPassword(user.getPassword());
        a.setSobrenome(user.getSobrenome());
        a.setStatus(user.getStatus());
        System.out.println(a.getId());
        userRepository.save(a);
        return "Data has been modified";
    }
    @PostMapping(path="/addUserAsAdmin")
    public @ResponseBody String addAdmin(@RequestBody UsuarioUI user ){
        
        try{
        if( userRepository.findByEmail(user.email).isEmpty() )
            return "User not found";
        }catch(Exception e){
            return "No user was added to the database";
        }
        a = userRepository.findByEmail(user.email).get(0);
        a.setIsAdmin(true);
        a.setAdminBeginDate(new Date());
        a.setAdminEndDate(null);
        userRepository.save(a);
        return "User permission has changed";
        
    }
    @PostMapping(path="/removeUserAsAdmin")
    public @ResponseBody String removeAdmin(@RequestBody UsuarioUI user){
        try{
        if( userRepository.findByEmail(user.email).isEmpty() )
            return "User not found";
        }catch(Exception e){
            return "No user was added to the database";
        }
        a = userRepository.findByEmail(user.email).get(0);
        a.setIsAdmin(false);
        a.setAdminEndDate(new Date());
        userRepository.save(a);
        
        return "User is no longer an admin";
    }

   
}
