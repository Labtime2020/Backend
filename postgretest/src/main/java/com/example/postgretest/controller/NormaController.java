/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.controller;
import com.example.postgretest.repository.NormaRepository;
import com.example.postgretest.repository.UserRepository;
import com.example.postgretest.util.Status;
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
public class NormaController {
    @Autowired
    NormaRepository normaRepository;
    
    @Autowired
    UserRepository userRepository;
   
    
    
    @PostMapping("/addNorma")
    public String  addNorma(){
        return "teste";
    }
    
}
