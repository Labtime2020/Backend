/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.model;

import com.example.postgretest.repository.ArquivoRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import com.example.postgretest.service.NormaService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.stereotype.Component;
/**
 *
 * @author labtime
 */

@Entity
@Table(name="norma", uniqueConstraints = @UniqueConstraint(columnNames = "nome", name = "uniqueName"))
public class Norma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long normaId;    
    @Column(length=200)
    @NotBlank
    private String nome;
    @Column(length=3000)
    @NotBlank
    private String descricao;
    @Column(length=200)
    private String url;
    
    @Column
    @Temporal(TemporalType.DATE)
    private Date registerDate;
    @Column
    @Temporal(TemporalType.DATE)
    private Date deletionDate;
    @OneToOne
    @JoinColumn(name="creationUser")
    private Usuario creationUser;
    @OneToOne
    @JoinColumn(name="deletionUser")
    private Usuario deletionUser;
    private boolean isActive;/*situacao da norma*/
    private int download;
    private int visualizacao;
    

    @ManyToMany(mappedBy="favoritos")
    private List<Usuario> usuarios = new ArrayList<Usuario>();

    @ManyToMany
    @JoinTable(name = "norma_tag",
            joinColumns = { @JoinColumn(name = "fk_norma")},
            inverseJoinColumns = { @JoinColumn(name = "fk_tag") })
    private List<Tag> tags = new ArrayList<Tag>();//tags dessa norma
    
    @OneToOne(mappedBy="norma")
    //@JoinColumn(name="fk_fid")
    private Arquivo arq;

    
    public List<Usuario> getUsuarios(){
    	return this.usuarios;
    }
    
    
    
    public Norma(long normaId, String nome, String descricao, String url, Date registerDate, Date deletionDate, 
    	Usuario creationUser, Usuario deletionUser, boolean isActive, int download, int visualizacao) {
        this.normaId = normaId;
        this.nome = nome;
        this.descricao = descricao;
        this.url = url;
        this.registerDate = registerDate;
        this.deletionDate = deletionDate;
        this.creationUser = creationUser;
        this.deletionUser = deletionUser;
        this.isActive = isActive;
        this.download = download;
        this.visualizacao = visualizacao;
    }
    
    public Norma(){
        
    }
    
    public NormaUI toNormaUI(){
        
        System.out.println("Qtde de visualizacoes>>>> " + this.visualizacao);
        NormaUI norm = new NormaUI(this.getNormaId(), this.getNome(), this.getDescricao(), this.getUrl(), this.isIsActive(), this.getVisualizacao(), this.getDownload());
        
        
        //norm.temArquivo = ( arquivoRepository.findByNorma(this.normaId).isEmpty() == true ) ? false : true;
            
        
        for(Tag tag: this.getTags()){
            norm.tags.add(tag.getNome());
        }

        return norm;
    }

    public Arquivo getArq() {
        return arq;
    }
    
    public int getDownload() {
        return download;
    }

    public int getVisualizacao() {
        return this.visualizacao;
    }
    

    public List<Tag> getTags(){
        return this.tags;
    }

    public long getNormaId() {
        return normaId;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getUrl() {
        return url;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public Date getDeletionDate() {
        return deletionDate;
    }

    public Usuario getCreationUser() {
        return creationUser;
    }

    public Usuario getDeletionUser() {
        return deletionUser;
    }
    public String getNormaName_File(){
        int i = 0;
        if( this.nome.contains(" ") == true ){
            i = this.nome.indexOf(" ");
            return "norma_" + this.nome.substring(0, i-1);
        }
        else
            return "norma_" + this.nome;
    }

    public boolean isIsActive() {
        return this.isActive;
    }

//    public String getArquivo() {
//        return arquivo;
//    }

    public void setArq(Arquivo arq) {
        this.arq = arq;
    }
    

    public void setNormaId(long normaId) {
        this.normaId = normaId;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public void setDeletionDate(Date deletionDate) {
        this.deletionDate = deletionDate;
    }

    public void setCreationUser(Usuario creationUser) {
        this.creationUser = creationUser;
    }

    public void setDeletionUser(Usuario deletionUser) {
        this.deletionUser = deletionUser;
    }
    
//    public void setArquivo(String arquivo) {
//        this.arquivo = arquivo;
//    }    
    
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public void setVisualizacao(int visualizacao) {
        this.visualizacao = visualizacao;
    }

    
    
}
