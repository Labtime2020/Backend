/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.Controller;
import com.example.postgretest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.postgretest.repository.UserRepository;
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
    public String insertData(){
        User t = new User(123, "teste","teste","teste","teste");
        t.setIsAdmin(false);
        userRepository.save(t);
        return "User has been added to the database";
    }
    
}
