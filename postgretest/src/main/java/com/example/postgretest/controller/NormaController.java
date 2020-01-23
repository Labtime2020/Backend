/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.Controller;

import com.example.postgretest.model.Norma;
import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.NormaUI;
import com.example.postgretest.model.TagUI;
import com.example.postgretest.repository.NormaRepository;
import com.example.postgretest.repository.TagRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.postgretest.repository.UserRepository;
import com.example.postgretest.service.EmailSenderService;
import com.example.postgretest.service.NormaService;
import com.example.postgretest.storage.FileSystemStorageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class NormaController {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    NormaRepository normaRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private EmailSenderService javaMailSender;
    private Optional<Norma> normaChk;
    private List<Usuario> userChk;
    private Norma normaObject;
    
    private final FileSystemStorageService storageService;
    
    @Autowired
    NormaService normaService;
    
    /*private void AtualizarEntrada(Authentication auth){
        if(auth != null){
            System.out.println(auth.getName() + " entrou!");
            
            Usuario user = userRepository.findByEmail(auth.getName()).get(0);
            user.atualizarEntrada();

            userRepository.save(user);
        }
    }*/

    @Autowired
    public NormaController(FileSystemStorageService storageService){
        this.storageService = storageService;
    }

    @GetMapping("/buscartags")
    public TagUI buscartags(Authentication auth){
        return normaService.buscartags(auth);
    }
    /*public TagUI buscartags(Authentication auth){
        AtualizarEntrada(auth);

        TagUI tags = new TagUI();
        List<Tag> mtags = tagRepository.findAll();

        for(Tag tg: mtags){
            tags.tags.add(tg.getNome());
        }

        return tags;
    }*/

    @PostMapping("/filtrarnormas")
    public Set<NormaUI> filtrarnormas(Authentication auth, @RequestBody TagUI tags){
        return normaService.filtrarnormas(auth, tags);
    }
    /*public Set<NormaUI> filtrarnormas(Authentication auth, @RequestBody TagUI tags){
        AtualizarEntrada(auth);

        Set<NormaUI> normas = new TreeSet<>();

        for(String tag: tags.tags){
            Tag tg = tagRepository.findByNome(tag).get();

            for(Norma norma: tg.getNormas()){
                normas.add(norma.toNormaUI());
            }
        }

        return normas;
    }*/

    @GetMapping("/buscarnormas")
    public List<NormaUI> buscarnormas(Authentication auth){
        return normaService.buscarnormas(auth);
    }
    /*public List<NormaUI> buscarnormas(Authentication auth){
        AtualizarEntrada(auth);

        List<Norma> norms = normaRepository.findAll();
        List<NormaUI> normas = new ArrayList<>();

        for(Norma norma: norms){
            normas.add(norma.toNormaUI());
        }

        return normas;
    }*/

    @PostMapping(path="/obterArquivoNorma")
    public ResponseEntity<Resource> obterArquivoNorma(Authentication auth, @RequestBody NormaUI norma){
        return normaService.obterArquivoNorma(auth, norma);
    }
    /*public ResponseEntity<Resource> obterArquivoNorma(Authentication auth, @RequestBody NormaUI norma){
        AtualizarEntrada(auth);

        Optional<Norma> n1 = normaRepository.findByNormaId(norma.getNormaId());
        if( n1.isEmpty() == false ){
            Resource file = storageService.loadAsResource(n1.get().getArquivo());
            Norma n2 = n1.get();
            n2.setDownload(n2.getDownload()+1);
            normaRepository.save(n2);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }
        else
            return null;
    }*/

    @PostMapping(path="/visualizarNorma")
    public  String visualizarNorma(Authentication auth, @RequestBody NormaUI norma){
        return normaService.visualizarNorma(auth, norma);
    }
    /*public  String visualizarNorma(Authentication auth, @RequestBody NormaUI norma){
        AtualizarEntrada(auth);

        Optional<Norma> nrm = normaRepository.findByNormaId(norma.getNormaId());
        if( nrm.isEmpty() == true){
            System.out.println("Norma nula");
            return null;
        }
        else{
            Norma n0 = nrm.get();
            n0.setVisualizacao(n0.getVisualizacao()+1);
            normaRepository.save(n0);
            //return ResponseEntity.status(HttpStatus.FOUND)
            //.location(URI.create(n0.getUrl()))
            //.build();
            return n0.getUrl();
        }
    }*/

    @PostMapping(path="/addNorma")
    public @ResponseBody Resposta addNorma( Authentication auth, @RequestParam(name="file", required=false) MultipartFile file,
            @RequestParam("norma") String n1, @RequestParam(value="checksum", required=false) String checksum ) throws JsonProcessingException, IOException{
        return normaService.addNorma(auth, file, n1, checksum);
    }
    /*public @ResponseBody Resposta addNorma( Authentication auth, @RequestParam(name="file", required=false) MultipartFile file,
            @RequestParam("norma") String n1 ) throws JsonProcessingException{
        AtualizarEntrada(auth);

       //com tempo, adicionar AQUI perfil de seguranca para permitir somente administradores
        ObjectMapper obj = new ObjectMapper();
        
        NormaUI norma = obj.readValue(n1, NormaUI.class);
        //mapeamento feito com sucesso!
    
        normaChk = normaRepository.findByNome(norma.getNome());
        
        if( !normaChk.isEmpty() )
            return new Resposta(NORMAJAEXISTE, ME15);
        
        else if( ( norma.getUrl() == null && file == null ) || ( norma.getUrl() == null && file.isEmpty() == true ) )
            return new Resposta(ERRO,ME17);
        else if(file != null && (file.getSize()/1024) >= 100024 ){
            return new Resposta(ERRO, ME_C_4);
        }
        else{
            //extraindo id pelo token
            userChk = userRepository.findByEmail(auth.getName());
            
            if( userChk.isEmpty() )
                return new Resposta(SEMUSER, "Nenhum usuario com este ID");
            
            else{
                Norma normaObject = new Norma(norma.getNormaId(), norma.getNome(), 
                    norma.getDescricao(), norma.getUrl(), new Date(), null, userChk.get(0), userChk.get(0), true, 0, 0);

                for(String tag: norma.tags){
                    Optional<Tag> test = tagRepository.findByNome(tag);
                    Tag tg;

                    if(test.isEmpty()){
                        tagRepository.save(new Tag(0, tag));
                        tg = tagRepository.findByNome(tag).get();
                    }else{
                        tg = test.get();
                    }

                    normaObject.getTags().add(tg);
                }

                if(file != null && file.isEmpty() == false){
                   normaObject.setArquivo(normaObject.getNormaName_File() + "." +
                    storageService.getExtensao(file.getOriginalFilename()));
                }

                normaRepository.save(normaObject);
                
                try{
                    storageService.salvar(file, normaObject.getArquivo());
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Falha ao salvar arquivo");
                }

                return new Resposta(OK, "Norma cadastrada com sucesso");
            }
        }
    }*/
    
    @PostMapping(path="/updateNorma")
    public @ResponseBody Resposta updateNorma( Authentication auth,
            @RequestParam(name="file", required=false) MultipartFile file ,
            @RequestParam("norma") String n1 ) throws JsonProcessingException{
        return normaService.updateNorma(auth, file, n1);
    }
    /*public @ResponseBody Resposta updateNorma( Authentication auth,
            @RequestParam(name="file", required=false) MultipartFile file ,
            @RequestParam("norma") String n1 ) throws JsonProcessingException{
        AtualizarEntrada(auth);

        ObjectMapper obj = new ObjectMapper();
        NormaUI norma = obj.readValue(n1, NormaUI.class);
        
        normaChk = normaRepository.findByNormaId(norma.getNormaId());
        if( normaChk.isEmpty() ){
            return new Resposta(NORMA_INEXISTENTE, ME_C_0);
        }
        else if( (normaChk.get().getArquivo() == null) && 
                 ((norma.getUrl() == null && file == null )|| ( norma.getUrl() == null && file.isEmpty() == true )) )
            return new Resposta(ERRO,ME17);
        else if( (file.getSize()/1024) >= 100024 ){
            return new Resposta(ERRO, ME_C_4);
        }
        else{
            Norma normaAntiga = normaChk.get();//salvo a norma antiga para enviar email com alteracoes
            String tmpPath = normaAntiga.getArquivo();
            
            try{
                Optional<Norma> norma1 = normaRepository.findByNome(norma.getNome());

                if(!norma1.isEmpty() &&
                    norma1.get().getNormaId() != norma.getNormaId()
                  )/*se ja existe uma norma com o nome fornecido e essa norma tem um id diferente da norma atual, aborte
                {
                    System.out.println(ME15 + ". Abortando...");
                    return new Resposta(NORMAJAEXISTE, ME15);
                }
                else{
                    normaObject = normaRepository.findByNormaId(norma.getNormaId()).get();
                    if( norma.getNome() != null)
                        normaObject.setNome(norma.getNome());
                    normaObject.setUrl(norma.getUrl());
                    normaObject.setDescricao(norma.getDescricao());

                    normaObject.getTags().clear();

                    for(String tag: norma.tags){
                        Optional<Tag> test = tagRepository.findByNome(tag);
                        Tag tg;

                        if(test.isEmpty()){
                            tagRepository.save(new Tag(0, tag));
                            tg = tagRepository.findByNome(tag).get();
                        }else{
                            tg = test.get();
                        }

                        normaObject.getTags().add(tg);
                    }

                    System.out.println(normaObject.getTags().size() + " Eh o numero de tags!!!!!!!");

                    if( file != null && file.isEmpty() == false ){
                        normaObject.setArquivo(normaObject.getNormaName_File() + "." +
                                               storageService.getExtensao(file.getOriginalFilename())
                        );
                    }
                    
                    if(normaObject.getUsuarios().isEmpty() == false){
                        Usuario iterator;
                        String msg;

                        for( int i = 0; i < normaObject.getUsuarios().size(); i++ ){
                            iterator = normaObject.getUsuarios().get(i);

                            msg = "Nome antigo: " + normaAntiga.getNome() 
                                  + "Nome novo: " + norma.getNome()
                                  + "\\r\\nEntre para ver as modificacoes.";
                            javaMailSender.sendEmailComModificacoes(iterator, msg);
                        }
                    }

                    normaRepository.save(normaObject);

                    try{
                        System.out.println(normaAntiga.getNormaName_File());
                        
                        
                        storageService.remover(tmpPath);
                        storageService.salvar(file, normaObject.getArquivo());
                        
                    }catch(Exception e){
                        System.out.println("Deu ruim");
                    }
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }   
        }
        return new Resposta(OK, MS01);
    }*/
    
    @PostMapping(path="/updateNormaStatus")
    public @ResponseBody Resposta updateStatus( Authentication auth, @RequestBody NormaUI norma){
        return normaService.updateStatus(auth, norma);
    }
        
    /*public @ResponseBody Resposta updateStatus( Authentication auth, @RequestBody NormaUI norma){
        AtualizarEntrada(auth);

        Optional<Norma> n = normaRepository.findByNormaId(norma.getNormaId());
        try{
            if( n.isEmpty() ){
            return new Resposta(NORMA_INEXISTENTE, ME_C_0);
            }
            else if( norma.isIsActive() == n.get().isIsActive() ){
                return new Resposta(ERRO, "Nao houve mudanca de estado");
            }//nao houve mudanca de estado
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
    }*/
}
