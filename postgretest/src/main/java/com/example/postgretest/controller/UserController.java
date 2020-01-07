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
/**
 *
 * @author labtime
 */
@RestController

public class UserController {
    @Autowired 
    private UserRepository userRepository;
    
    @GetMapping(path="/addUser")
    //public @ResponseBody String insertUser(@RequestParam String nome, @RequestParam String email, @RequestParam String sobrenome, @RequestParam String password){
    public @ResponseBody String insertData(){
        Usuario t = new Usuario(1, "andre", "andre@gmail.com", "boy", "andre123", true);
        t.setRegisterDate(new Date());/*falta converter para a data atual*/
        userRepository.save(t);
        return "User has been added to the database";
    }
    
}
