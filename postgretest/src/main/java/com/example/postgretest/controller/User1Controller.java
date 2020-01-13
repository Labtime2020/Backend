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

@RestController
public class User1Controller {
    private final FileSystemStorageService storageService;

    @Autowired
    private DesbloqueioTokenRepository desbloqueioTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService javaMailSender;

    @Autowired
    private NormaRepository normaRepository;
    
    @Autowired
    public User1Controller(FileSystemStorageService storageService){
        this.storageService = storageService;
    }

    @PostMapping("/obter_avatar_usuario")
    @ResponseBody
    public ResponseEntity<Resource> obter_avatar_usuario(@RequestBody UsuarioUI usuario) {
        Usuario user = userRepository.findByEmail(usuario.email).get(0);

        Resource file = storageService.loadAsResource(user.getAvatar());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/obter_minha_avatar")
    public ResponseEntity<Resource> obter_minha_avatar(Authentication auth){
        Usuario user = userRepository.findByEmail(auth.getName()).get(0);

        Resource file = storageService.loadAsResource(user.getAvatar());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/cadastrar")
    public Resposta cadastrar(@RequestParam("file") MultipartFile file, @RequestParam("usuario") String usuarioString) 
    throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        UsuarioUI usuario = mapper.readValue(usuarioString, UsuarioUI.class);

    	System.out.println("Cadastrando Usuario");

    	List<Usuario> users = userRepository.findByEmail(usuario.email);
    	
    	if(users.size() > 0){
    		return new Resposta(ERRO, "Usuario com mesmo email ja cadastrado");
    	}else{
    		Usuario nuser = new Usuario(123, usuario.nome, usuario.email, usuario.sobrenome, usuario.password, usuario.isAdmin, ATIVO);

    		List<Usuario> test = userRepository.findAll();
	
			if(test.size() == 0){
				nuser.setIsAdmin(true);

				nuser.setAdminBeginDate(new Date());
			}	

            nuser.setAvatar("avatar_" + usuario.email + "." 
                        + storageService.getExtensao(file.getOriginalFilename()));

            userRepository.save(nuser);

            try{

                storageService.salvar(file, nuser.getAvatar());
            }catch(Exception ex){
                return new Resposta(ERRO, "Falha ao salvar avatar");
            }

    		return new Resposta(OK, "Usuario criado com sucesso");
    	}
    }

    @PostMapping("/adicionarfavorito")
    public Resposta adicionarfavorito(Authentication auth, @RequestBody NormaUI norma){
        try{
            Norma nor = normaRepository.findByNome(norma.nome).get();
            System.out.println("aqui");
            System.out.println(auth.getName()+"\n\n\n\n\n\n");
            Usuario user = userRepository.findByEmail(auth.getName()).get(0);
            user.getFavoritos().add(nor);
            userRepository.save(user);
        }catch(Exception e){
            System.out.println("erro" + e.getMessage());
        }
        return new Resposta(OK, "favoritado com sucesso");
    }

    @PostMapping("/removerfavorito")
    public Resposta removerfavorito(Authentication auth, @RequestBody NormaUI norma){
        Usuario user = userRepository.findByEmail(auth.getName()).get(0);

        for(int i = 0 ; i < user.getFavoritos().size() ; i++){
            System.out.println(user.getFavoritos().get(i).getNome() + " == " + norma.nome);

            if(user.getFavoritos().get(i).getNormaId() == norma.normaId){
                 System.out.println("removendo...");
                user.getFavoritos().remove(i);
                break;
            }
        }

        userRepository.save(user);

        return new Resposta(OK, "desfavoritado com sucesso");
    }

    @PostMapping("/listarfavoritos")
    public List<NormaUI> listarfavoritos(Authentication auth){
        Usuario user = userRepository.findByEmail(auth.getName()).get(0);

        List<NormaUI> favoritos = new ArrayList<>();

        for(Norma norma: user.getFavoritos()){
            favoritos.add(new NormaUI(norma.getNormaId(), norma.getNome(), norma.getDescricao(), norma.getUrl(), norma.isIsActive()));
        }

        return favoritos;
    }

    @GetMapping("/buscarusuarios")
    public List<UsuarioUI> buscarusuarios(){
    	System.out.println("Buscando todos os usuarios");

    	List<Usuario> users = userRepository.findAll();
    	List<UsuarioUI> usuarios = new ArrayList<>();

    	for(Usuario user: users){
    		usuarios.add(new UsuarioUI(user.getId(), user.getEmail(), user.getNome(), user.getSobrenome(),
    			user.getIsAdmin(), user.getPassword(), user.getStatus()));
    	}

    	return usuarios;
    }

    @GetMapping("/incrementar_erro/{email}")
    public String incrementar_erro(@PathVariable String email){
        Usuario user = userRepository.findByEmail(email).get(0);
        user.addTentativaErrada();

        userRepository.save(user);

        if(user.getTentativaErrada() >= MAX_NUM_TENTATIVAS){
            //enviar email com instrucoes para usuario!.
            emailSenderService.sendDesbloqueioToken(user);
            return ME10_1;
        }

        return ME09;
    }

    @GetMapping("/desbloquear")
    public String desbloquear(@RequestParam("token")String desbloqueioToken){
        DesbloqueioToken token = desbloqueioTokenRepository.findByDesbloqueioToken(desbloqueioToken);

        if(token != null){
            Usuario user = userRepository.findByEmail(token.getUsuario().getEmail()).get(0);
            
            if(user.getTentativaErrada() < MAX_NUM_TENTATIVAS){
                return ME11;
            }

            user.zerarTentativaErrada();
            userRepository.save(user);

            return MS02;
        }else{
            return ME12;
        }
    }
}