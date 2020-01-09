/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.Controller;
import com.example.postgretest.model.Norma;
import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.UsuarioUI;
import com.example.postgretest.model.NormaUI;
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
import static com.example.postgretest.util.Status.*;
import java.util.Date;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestBody;
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
   
    private Optional<Norma> normaChk; 
    private Norma normaObject;
    @PostMapping(path="/addNorma")
    public @ResponseBody Resposta addNorma( @RequestBody NormaUI norma ){
       
        normaChk = normaRepository.findByNome(norma.getNome());
        
        if( !normaChk.isEmpty() ){
            return new Resposta(ERRO, "Falha");
        }
        else{
            Usuario usr = userRepository.findById(norma.getCreationUser()).get();//obtnho o usuario pelo repositorio.
            System.out.println(usr.getAdminBeginDate() + "blablablabla nao eh nulo");
            //normaObject = new Norma(long id, norma.getNome(), null, new Date(), null, norma.getCreationUser(), norma.getDeletionUser());
            
            normaRepository.save(normaObject);
            return new Resposta(OK, "teste certo");
        }
    }
    
}
