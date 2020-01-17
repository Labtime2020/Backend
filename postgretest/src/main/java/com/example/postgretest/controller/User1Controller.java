package com.example.postgretest.Controller;

import java.io.IOException;
import java.util.stream.Collectors;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;

import com.example.postgretest.model.DesbloqueioToken;
import com.example.postgretest.model.RedefinirSenhaToken;
import com.example.postgretest.repository.RedefinirSenhaTokenRepository;
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
import com.example.postgretest.security.TokenAuthenticationService;

import com.example.postgretest.storage.StorageFileNotFoundException;
import com.example.postgretest.storage.FileSystemStorageService;

import java.util.Date;
import java.time.LocalDate;

import static com.example.postgretest.util.Status.*;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
@RestController
public class User1Controller {
    private final FileSystemStorageService storageService;

    @Autowired
    private DesbloqueioTokenRepository desbloqueioTokenRepository;

    @Autowired
    private RedefinirSenhaTokenRepository redefinirSenhaTokenRepository;

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

    private void AtualizarEntrada(Authentication auth){
        if(auth != null){
            Usuario user = userRepository.findByEmail(auth.getName()).get(0);
            user.atualizarEntrada();

            userRepository.save(user);
        }
    }

    @PostMapping("/hey")
    public @ResponseBody String teste(){
        return "Ola mundo";
    }

    @PostMapping("/buscarusuariosonline")
    public List<UsuarioUI> buscarusuariosonline(Authentication auth, @RequestBody String email){    
        // AtualizarEntrada(auth);

        List<Usuario> users = userRepository.findByOnline(true);

        System.out.println(users.size());
        List<UsuarioUI> list = new ArrayList<>();

        Date now = new Date();

        for(Usuario user: users){
            long diff = now.getTime() - user.getLastInteractionDate().getTime();

            System.out.println(diff + " x " + TEMPO_ONLINE);

            if(diff <= TEMPO_ONLINE){
                list.add(user.toUsuarioUI());
            }
            else{
                user.setOnline(false);
                userRepository.save(user);
            }
        }

        return list;
    }

    @PostMapping("/buscarusuarioporemail")
    public List<UsuarioUI> buscarusuarioporemail(Authentication auth, @RequestBody String email){    
        AtualizarEntrada(auth);

        List<Usuario> users = userRepository.findByEmailContaining(email);

        System.out.println(users.size());
        List<UsuarioUI> list = new ArrayList<>();

        for(Usuario user: users){
            list.add(user.toUsuarioUI());
        }

        return list;
    }

    @PostMapping("/obteravatarusuario")
    @ResponseBody
    public ResponseEntity<Resource> obter_avatar_usuario(Authentication auth, @RequestBody UsuarioUI usuario) {
        AtualizarEntrada(auth);

        Usuario user = userRepository.findByEmail(usuario.email).get(0);

        Resource file = storageService.loadAsResource(user.getAvatar());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/obterminhaavatar")
    public ResponseEntity<Resource> obter_minha_avatar(Authentication auth){
        Usuario user = userRepository.findByEmail(auth.getName()).get(0);

        Resource file = storageService.loadAsResource(user.getAvatar());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/cadastrar")
    public Resposta cadastrar(Authentication auth, @RequestParam(name="file", required=false) MultipartFile file,
        @RequestParam("usuario") String usuarioString) 
            throws JsonProcessingException{
        AtualizarEntrada(auth);

        ObjectMapper mapper = new ObjectMapper();
        
        UsuarioUI usuario = mapper.readValue(usuarioString, UsuarioUI.class);

    	System.out.println("Cadastrando Usuario");

    	List<Usuario> users = userRepository.findByEmail(usuario.email);
    	
    	if(users.size() > 0){
    		return new Resposta(USERJAEXISTE, ME04_2);
    	}else{
    		Usuario nuser = new Usuario(123, usuario.nome, usuario.email, usuario.sobrenome, usuario.password, usuario.isAdmin, ATIVO);

    		List<Usuario> test = userRepository.findAllByOrderByNome();
	
			if(test.size() == 0){
				nuser.setIsAdmin(true);

				nuser.setAdminBeginDate(new Date());
			}	

            nuser.setRegisterDate(new Date());

            nuser.setAvatar("avatar_not_found.jpg");

            if(file != null)
                nuser.setAvatar("avatar_" + usuario.email + "." 
                        + storageService.getExtensao(file.getOriginalFilename()));

            userRepository.save(nuser);

            try{
                if(file != null)
                    storageService.salvar(file, nuser.getAvatar());
            }catch(Exception ex){
                return new Resposta(ERRO, "Falha ao salvar avatar");
            }

    		return new Resposta(OK, MS01);
    	}
    }

    @PostMapping("/adicionarfavorito")
    public Resposta adicionar_favorito(Authentication auth, @RequestBody NormaUI norma){
        AtualizarEntrada(auth);

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
    public Resposta remover_favorito(Authentication auth, @RequestBody NormaUI norma){
        AtualizarEntrada(auth);

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
        AtualizarEntrada(auth);

        Usuario user = userRepository.findByEmail(auth.getName()).get(0);

        List<NormaUI> favoritos = new ArrayList<>();

        for(Norma norma: user.getFavoritos()){
            favoritos.add(new NormaUI(norma.getNormaId(), norma.getNome(), norma.getDescricao(), norma.getUrl(), norma.isIsActive(), norma.getVisualizacao(), norma.getDownload()));
        }

        return favoritos;
    }

    @GetMapping("/buscarusuarios")
    public List<UsuarioUI> buscarusuarios(Authentication auth){
        AtualizarEntrada(auth);

    	System.out.println("Buscando todos os usuarios");

    	List<Usuario> users = userRepository.findAllByOrderByNome();
    	List<UsuarioUI> usuarios = new ArrayList<>();

    	for(Usuario user: users){
    		usuarios.add(user.toUsuarioUI());
    	}

    	return usuarios;
    }

    @PostMapping("/recuperarsenha")
    public Resposta recuperarsenha(Authentication auth, @RequestBody String email){
        AtualizarEntrada(auth);

        List< Usuario > user = userRepository.findByEmail(email);

        if(user.size() == 0){
            return new Resposta(ERRO, "nao existe usuario cadastrado para esse email");
        }

        Optional< RedefinirSenhaToken > token = redefinirSenhaTokenRepository.findByUserId(user.get(0).getId());

        if(!token.isEmpty()){
            redefinirSenhaTokenRepository.delete(token.get());
        }

        emailSenderService.sendRedefinirSenhaToken(user.get(0));
        
        return new Resposta(OK, "um email foi enviado com as instrucoes para redefinicao de senha");
    }

    @GetMapping("/redefinirsenha")
    public Resposta redefinirsenha(Authentication auth, HttpServletResponse response, @RequestParam("token")String mtoken){
        AtualizarEntrada(auth);

        RedefinirSenhaToken token = redefinirSenhaTokenRepository.findByRedefinirSenhaToken(mtoken);

        if(token != null){
            TokenAuthenticationService.addAuthentication(response, token.getUsuario().getEmail(), 
                AuthorityUtils.createAuthorityList("ROLE_USER"));//passe um token de autenticacao

            return new Resposta(OK, "Token retornado no header");
        }else{
            return new Resposta(ERRO, "token invalido");
        }
    }

    @PostMapping(path="/alterarsenha")
    public @ResponseBody Resposta alterarsenha(Authentication auth, @RequestBody String novaSenha){//precisa de autenticacao...
        AtualizarEntrada(auth);
        System.out.println(auth.getName() + " eh o email");

        Usuario user = userRepository.findByEmail(auth.getName()).get(0);

        System.out.println(user.getPassword() + " == " + novaSenha);

        if(user.getPassword().equals(novaSenha)){
            return new Resposta(MESMASENHA, ME19);
        }
        

        user.setPassword(novaSenha);

        userRepository.save(user);

        return new Resposta(OK, MS01 + "senha atualizada com sucesso");
    }

    @GetMapping("/incrementar_erro/{email}")
    public String incrementar_erro(Authentication auth, HttpServletResponse response, @PathVariable String email){
        AtualizarEntrada(auth);

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
    public String desbloquear(Authentication auth, @RequestParam("token")String desbloqueioToken){
        AtualizarEntrada(auth);
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
