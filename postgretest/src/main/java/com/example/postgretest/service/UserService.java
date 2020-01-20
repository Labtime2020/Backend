/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.service;

import com.example.postgretest.Controller.Resposta;
import com.example.postgretest.model.DesbloqueioToken;
import com.example.postgretest.model.Norma;
import com.example.postgretest.model.NormaUI;
import com.example.postgretest.model.RedefinirSenhaToken;
import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.UsuarioUI;
import com.example.postgretest.repository.DesbloqueioTokenRepository;
import com.example.postgretest.repository.NormaRepository;
import com.example.postgretest.repository.RedefinirSenhaTokenRepository;
import com.example.postgretest.repository.UserRepository;
import com.example.postgretest.security.TokenAuthenticationService;
import com.example.postgretest.service.EmailSenderService;
import com.example.postgretest.storage.FileSystemStorageService;
import static com.example.postgretest.util.Status.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author labtime
 */
@Service("userService")
public class UserService {
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
    private NormaRepository normaRepository;
    
    private Usuario a;
    private Optional <Usuario> b;
    
    @Autowired
    public UserService(FileSystemStorageService storageService){
        this.storageService = storageService;
    }
    
    
    private boolean isAdmin( Authentication auth ) throws Exception{
        List<Usuario> users = userRepository.findByEmail(auth.getName());
        if( users.isEmpty() )
            throw new Exception("Nao existe usuario com este id");
        else{
            users.get(0).atualizarEntrada();
            userRepository.save(users.get(0));

            return (users.get(0).getIsAdmin() == true) ? true : false;
        }
    }
    
    public @ResponseBody Resposta updateMyData(Authentication auth, MultipartFile file, String usuarioString,
        HttpServletResponse response) throws JsonProcessingException{
        List<Usuario> usuarioLogado = userRepository.findByEmail(auth.getName());
        ObjectMapper mapper = new ObjectMapper();
        UsuarioUI user = mapper.readValue(usuarioString, UsuarioUI.class);
        
        if(usuarioLogado.isEmpty() == false){
            Usuario userAtual = usuarioLogado.get(0);
            userAtual.atualizarEntrada();

            List<Usuario> outros = userRepository.findByEmail(user.getEmail());
            if(outros.isEmpty() == false && outros.get(0).getId() != userAtual.getId() ){
                System.out.println("Update de email nao sera feito, pois ja existe usuario cadastrado!");
                return new Resposta(USERJAEXISTE, ME04_2);
            }
            else{
                userAtual.setEmail(user.getEmail());
                
                if( user.getNome()!=null && (!user.getNome().isBlank() || !user.getNome().isEmpty()) )
                    userAtual.setNome(user.getNome());
                if( user.getSobrenome() != null &&( !user.getSobrenome().isBlank() || !user.getSobrenome().isEmpty() ) )
                    userAtual.setSobrenome(user.getSobrenome());
                if(file != null)
                    userAtual.setAvatar("avatar_" + user.email + "." 
                            + storageService.getExtensao(file.getOriginalFilename()));
            }
            userRepository.save(userAtual);
            
            try{
                if(file != null)
                    storageService.salvar(file, userAtual.getAvatar());
            }catch(Exception ex){
                return new Resposta(ERRO, "Falha ao salvar avatar");
            }
            if( auth.getName().equals( user.getEmail() ) == false ){//houve mudanca de email... envie um novo token
                TokenAuthenticationService.addAuthentication(response, userAtual.getEmail(), auth.getAuthorities());
                System.out.println("Mudanca de token!");
            }
            System.out.println(userAtual.getId());
            return new Resposta(OK, "User information updated");
        }
        
        return new Resposta(SEMUSER, "Nada a fazer com este usuario!");
    }
    
    public @ResponseBody Resposta updateData(MultipartFile file, 
        Authentication auth, String usuarioString)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UsuarioUI user = mapper.readValue(usuarioString, UsuarioUI.class);

        Usuario usuarioLogado = userRepository.findByEmail(auth.getName()).get(0);

        usuarioLogado.atualizarEntrada();
        userRepository.save(usuarioLogado);

        System.out.println(user.getEmail());
        
        Optional<Usuario> c = userRepository.findById(user.getId());
        
        if( c.isEmpty() ){
            return new Resposta(SEMUSER, "Nao foi encontrado usuario com este email!");
        }
        /*
        else if( c.get().getEmail().equals(user.getEmail()) == false){ //ja existe user com email fornecido, abortar
            System.out.println(c.get().getEmail() + " Este usuario ja existe no sistema ");
            return new Resposta(USERJAEXISTE, ME04_2);
        }*/
        try{
            a = c.get();
            a.setEmail(user.getEmail());
            if( user.getNome()!= null || !user.getNome().isEmpty() || !user.getNome().isBlank() )
                a.setNome(user.getNome());
            if( user.getSobrenome() != null &&( !user.getSobrenome().isBlank() || !user.getSobrenome().isEmpty() ) )
                a.setSobrenome(user.getSobrenome());
            a.setAvatar("avatar_" + user.email + "." 
                        + storageService.getExtensao(file.getOriginalFilename()));

            userRepository.save(a);

            try{
                storageService.salvar(file, a.getAvatar());
            }catch(Exception ex){
                return new Resposta(ERRO, "Falha ao salvar avatar");
            }

            System.out.println(a.getId());
        }catch(Exception e){
            return new Resposta(ERRO, e.toString());
        }

        return new Resposta(OK, "User information updated");
    }
    
    public @ResponseBody Resposta addAdmin(Authentication auth, UsuarioUI user ){
        try{/*Checagem de seguranca, somente usuarios administradores podem adicionar outros users como admin*/
            if(isAdmin(auth) == false)
                return new Resposta(ERRO,ME_C_1);
        }
        catch(Exception e){
            e.getMessage();
        }
        
        List<Usuario> c = userRepository.findByEmail(user.getEmail());
        
        if( c.isEmpty() )
            return new Resposta(SEMUSER, "Nao foi encontrado usuario com este id!");
        
        Usuario usuariolog = userRepository.findByEmail(auth.getName()).get(0);

        usuariolog.atualizarEntrada();
        userRepository.save(usuariolog);

        a = c.get(0);
        a.setIsAdmin(true);
        a.setAdminBeginDate(new Date());
        a.setAdminEndDate(null);
        userRepository.save(a);
      
        return new Resposta(OK, "Usuario agora eh um administrador!");
    }
    
    public @ResponseBody Resposta removeAdmin(Authentication auth, UsuarioUI user){
        try{/*Checagem de seguranca, somente usuarios administradores podem adicionar outros users como admin*/
            if(isAdmin(auth) == false)
                return new Resposta(ERRO,ME_C_1);
        }
        catch(Exception e){
            e.getMessage();
        }
        
        b = userRepository.findById(user.id);
        
        try{
            if( b.isEmpty() )
                return new Resposta(SEMUSER, "Nao foi encontrado usuario com este id!");
            if( userRepository.findByIsAdmin(true).size() <= 1 )
                return new Resposta(UNADMIN, "Voce nao pode remover o unico administrador do sistema");
        }catch(Exception e){
            return new Resposta(ERRO, "Falha de comportamento!");
        }
        
        Usuario userlog = userRepository.findByEmail(auth.getName()).get(0);
        
        userlog.atualizarEntrada();
        userRepository.save(userlog);

        a = userRepository.findByEmail(user.email).get(0);
        a.setIsAdmin(false);
        a.setAdminEndDate(new Date());
        a.atualizarEntrada();
        userRepository.save(a);
        
        return new Resposta(OK, "Usuario nao eh mais administrador");
    }
    
    public @ResponseBody Resposta updateUserStatus(Authentication auth, UsuarioUI user){
        
        if( user.getEmail() == null || user.getStatus() > 1 || user.getStatus() < -1 ){
                return new Resposta(ERRO, JSONINVALIDO);
        }
        List<Usuario> userChk = userRepository.findByEmail(user.getEmail());
        if( userChk.isEmpty() ){
            return new Resposta(SEMUSER, ME_C_2);
        }
        else{
            Usuario usuariolog = userRepository.findByEmail(auth.getName()).get(0);

            usuariolog.atualizarEntrada();
            userRepository.save(usuariolog);

            Usuario usuario = userChk.get(0);
            if( user.getStatus() == 0 ){
                if( auth.getName() == user.getEmail() ){//usuario esta querendo se auto desativar
                    return new Resposta(ERRO, ME22);
                }
                else if( userRepository.findByIsAdmin(true).size() == 1
                        && usuario.getIsAdmin() == true ){
                    return new Resposta(UNADMIN, ME05);
                }
                else if( usuario.getIsAdmin() == true ){
                    usuario.setIsAdmin(false);
                    usuario.setAdminEndDate(new Date());
                }
                else
                    usuario.setStatus(0);
            }
            else{/*ativar usuario*/
                usuario.setStatus(1);
            }
            userRepository.save(usuario);
            
            return new Resposta(OK, MS01);
        }
    }
    
    /*here`s 
    ******
    ******
    ******
    ******
    user1controller*/
    
    private void AtualizarEntrada(Authentication auth){
        if(auth != null){
            Usuario user = userRepository.findByEmail(auth.getName()).get(0);
            user.atualizarEntrada();

            userRepository.save(user);
        }
    }
    public List<UsuarioUI> buscarusuariosonline(Authentication auth, String email){    
        // AtualizarEntrada(auth);

        List<Usuario> users = userRepository.findByOnline(true);

        System.out.println(users.size());
        List<UsuarioUI> list = new ArrayList<>();

        Date now = new Date();

        for(Usuario user: users){
            long diff = 0;
            //long diff = now.getTime() - user.getLastInteractionDate().getTime();

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
    
    public Resposta cadastrar(Authentication auth,  MultipartFile file,
        String usuarioString) 
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
    
    public List<UsuarioUI> buscarusuarioporemail(Authentication auth, String email){    
        AtualizarEntrada(auth);

        List<Usuario> users = userRepository.findByEmailContaining(email);

        System.out.println(users.size());
        List<UsuarioUI> list = new ArrayList<>();

        for(Usuario user: users){
            list.add(user.toUsuarioUI());
        }

        return list;
    }
    
    public ResponseEntity<Resource> obter_avatar_usuario(Authentication auth, UsuarioUI usuario) {
        AtualizarEntrada(auth);

        Usuario user = userRepository.findByEmail(usuario.email).get(0);

        Resource file = storageService.loadAsResource(user.getAvatar());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    public ResponseEntity<Resource> obter_minha_avatar(Authentication auth){
        Usuario user = userRepository.findByEmail(auth.getName()).get(0);

        Resource file = storageService.loadAsResource(user.getAvatar());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    public Resposta adicionar_favorito(Authentication auth, NormaUI norma){
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
    
    public Resposta remover_favorito(Authentication auth, NormaUI norma){
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
    
    public List<NormaUI> listarfavoritos(Authentication auth){
        AtualizarEntrada(auth);

        Usuario user = userRepository.findByEmail(auth.getName()).get(0);

        List<NormaUI> favoritos = new ArrayList<>();

        for(Norma norma: user.getFavoritos()){
            favoritos.add(new NormaUI(norma.getNormaId(), norma.getNome(), norma.getDescricao(), norma.getUrl(), norma.isIsActive(), norma.getVisualizacao(), norma.getDownload()));
        }

        return favoritos;
    }
    
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
    
    public Resposta recuperarsenha(Authentication auth, String email){
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
    
    public Resposta redefinirsenha(Authentication auth, HttpServletResponse response, String mtoken){
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
    
    public @ResponseBody Resposta alterarsenha(Authentication auth, String novaSenha){//precisa de autenticacao...
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
    
    public String desbloquear(Authentication auth, String desbloqueioToken){
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
