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
import com.example.postgretest.service.EmailSenderService;
import static com.example.postgretest.util.Status.*;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
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
    @Autowired
    private EmailSenderService javaMailSender;
    private Optional<Norma> normaChk;
    private List<Usuario> userChk;
    private Norma normaObject;
    
    @PostMapping(path="/addNorma")
    public @ResponseBody Resposta addNorma( Authentication auth, @RequestBody NormaUI norma ){
       /*com tempo, adicionar AQUI perfil de seguranca para permitir somente administradores*/
        normaChk = normaRepository.findByNome(norma.getNome());
        
        if( !normaChk.isEmpty() )
            return new Resposta(NORMAJAEXISTE, ME15);
        else if( norma.getUrl() == null )
            return new Resposta(URLNULO,ME17);
        else{
            //extraindo id pelo token*/
            userChk = userRepository.findByEmail(auth.getName());
            
            if( userChk.isEmpty() )
                return new Resposta(SEMUSER, "Nenhum usuario com este ID");
            
            else{
                Norma normaObject = new Norma(norma.getNormaId(), norma.getNome(), norma.getDescricao(), norma.getUrl(), new Date(), null, userChk.get(0), userChk.get(0), true);
                normaRepository.save(normaObject);
                return new Resposta(OK, "Norma cadastrada com sucesso");
            }
        }
    }
    
    @PostMapping(path="/updateNorma")
    public @ResponseBody Resposta updateNorma( Authentication auth, @RequestBody NormaUI norma ){
        
        normaChk = normaRepository.findByNormaId(norma.getNormaId());
        if( normaChk.isEmpty() ){
            return new Resposta(NORMA_INEXISTENTE, ME_C_0);
        }
        
        else if(norma.getUrl() == null ){
            return new Resposta(URLNULO, ME17);
        }
        else{
            Norma normaAntiga = normaChk.get();//salvo a norma antiga para enviar email com alteracoes
            try{
                if(!normaRepository.findByNome(norma.getNome()).isEmpty() &&
                    normaRepository.findById(norma.getNome()).get().getNormaId() != norma.getNormaId()
                  )/*se ja existe uma norma com o nome fornecido e essa norma tem um id diferente da norma atual, aborte*/
                {
                    System.out.println(ME15 + ". Abortando...");
                    return new Resposta(NORMAJAEXISTE, ME15);
                }
                else{
                    normaObject = normaRepository.findByNormaId(norma.getNormaId()).get();
                    normaObject.setNome(norma.getNome());
                    normaObject.setUrl(norma.getUrl());
                    normaObject.setDescricao(norma.getDescricao());
                    /*devemos enviar e-mail para os usuarios*/

                    if(normaObject.getUsuarios().isEmpty() == false){
                        Usuario iterator;
                        String msg;

                        for( int i = 0; i < normaObject.getUsuarios().size(); i++ ){
                            iterator = normaObject.getUsuarios().get(i);

                            msg = "Nome antigo: " + normaAntiga.getNome() + "Nome novo: " + norma.getNome();
                            javaMailSender.sendEmailComModificacoes(iterator, msg);
                        }
                    }
                    normaRepository.save(normaObject);
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }   
        }
        return new Resposta(OK, MS01);
    }
    
    @PostMapping(path="/updateNormaStatus")
    public @ResponseBody Resposta updateStatus( Authentication auth, @RequestBody NormaUI norma){
        Optional<Norma> n = normaRepository.findByNormaId(norma.getNormaId());
        try{
            if( n.isEmpty() ){
            return new Resposta(NORMA_INEXISTENTE, ME_C_0);
            }
            else if( norma.isIsActive() == n.get().isIsActive() ){
                return new Resposta(ERRO, "Nao houve mudanca de estado");
            }/*nao houve mudanca de estado*/
            else{
                System.out.println("Entrou!");
                Norma n1 = n.get();
                if( norma.isIsActive() == false ){//seto a data e usuarios de delecao
                    n1.setDeletionDate(new Date());
                    n1.setDeletionUser(userRepository.findByEmail(auth.getName()).get(0));
                }
                else{
                    n1.setDeletionDate(null);
                }
                n1.setIsActive(norma.isIsActive());
                 normaRepository.save(n1);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
       
        return new Resposta(OK,MS01);
    }
}
