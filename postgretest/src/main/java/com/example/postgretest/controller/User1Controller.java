package com.example.postgretest.Controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;

import com.example.postgretest.repository.RedefinirSenhaTokenRepository;
import com.example.postgretest.repository.DesbloqueioTokenRepository;
import com.example.postgretest.service.EmailSenderService;
import com.example.postgretest.model.UsuarioUI;
import com.example.postgretest.model.NormaUI;
import com.example.postgretest.model.UsuarioInfoUI;
import com.example.postgretest.repository.UserRepository;
import com.example.postgretest.repository.NormaRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.postgretest.service.UserService;

import com.example.postgretest.storage.FileSystemStorageService;


import static com.example.postgretest.util.Status.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins="http://localhost:9090", allowedHeaders = "*")
@RestController
public class User1Controller {
    private final FileSystemStorageService storageService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DesbloqueioTokenRepository desbloqueioTokenRepository;

    @Autowired
    private RedefinirSenhaTokenRepository redefinirSenhaTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private NormaRepository normaRepository;
    
    @Autowired
    public User1Controller(FileSystemStorageService storageService){
        this.storageService = storageService;
    }

    //add role here
    @PostMapping("/hey")
    public @ResponseBody String teste(){
        return "Ola mundo";
    }

    @PostMapping("/buscarusuariosonline")
    public List<UsuarioInfoUI> buscarusuariosonline(Authentication auth){    
        return userService.buscarusuariosonline(auth, MA01);
    }
    
    @PostMapping("/buscarusuarioporemail")
    public List<UsuarioInfoUI> buscarusuarioporemail(Authentication auth, @RequestBody String email){
        return userService.buscarusuarioporemail(auth, email);
    }


    @PostMapping("/obteravatarusuario")
    @ResponseBody
    public ResponseEntity<Resource> obter_avatar_usuario(Authentication auth, @RequestBody UsuarioUI usuario) {
        return userService.obter_avatar_usuario(auth, usuario);
    }
    

    @PostMapping("/obterminhaavatar")
    public ResponseEntity<Resource> obter_minha_avatar(Authentication auth){
        return userService.obter_minha_avatar(auth);
    }

    @PostMapping("/cadastrar")
    public Resposta createUser(Authentication auth, @RequestParam(name="file", required=false) MultipartFile file,
        @RequestParam("usuario") String usuarioString) throws JsonProcessingException{
        return userService.cadastrar(auth, file, usuarioString);
    }

    @PostMapping("/adicionarfavorito")
    public Resposta addFavorite(Authentication auth, @RequestBody NormaUI norma){
        //atualizar entrada
        return userService.adicionar_favorito(auth, norma);
    }

    @PostMapping("/removerfavorito")
    public Resposta remover_favorito(Authentication auth, @RequestBody NormaUI norma){
        return userService.remover_favorito(auth, norma);
    }

    @PostMapping("/listarfavoritos")
    public List<NormaUI> listarfavoritos(Authentication auth){
        return userService.listarfavoritos(auth);
    }
    

    @GetMapping("/buscarusuarios")
    public List<UsuarioInfoUI> buscarusuarios(Authentication auth){
        return userService.buscarusuarios(auth);
    }
    

    @PostMapping("/recuperarsenha")
    public Resposta recuperarsenha(Authentication auth, @RequestBody String email){
        return userService.recuperarsenha(auth, email);
    }
    

    @GetMapping("/redefinirsenha")
    public Resposta redefinirsenha(Authentication auth, HttpServletResponse response, @RequestParam("token")String mtoken){
        return userService.redefinirsenha(auth, response, mtoken);
    }
    

    @PostMapping(path="/alterarsenha")
    public @ResponseBody Resposta alterarsenha(Authentication auth, @RequestBody String novaSenha){
        return userService.alterarsenha(auth, novaSenha);
    }
    

    @GetMapping("/desbloquear")
    public String desbloquear(Authentication auth, @RequestParam("token")String desbloqueioToken){
        return userService.desbloquear(auth, desbloqueioToken);
    }
    
}
