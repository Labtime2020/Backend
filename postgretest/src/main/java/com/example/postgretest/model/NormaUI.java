/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.model;

import java.util.*;

/**
 *
 * @author labtime
 */
public class NormaUI implements Comparable<NormaUI>{
    public long normaId;
    public String nome;
    public String descricao;
    //public long creationUser;//id do usuario que criou a norma
    //public long deletionUser;//id do usuario que removeu a norma
    public String url;
    public boolean isActive;
    public List< String > tags = new ArrayList<>();
    public int visualizacao;

    public NormaUI(long normaId, String nome, String descricao, String url, boolean isActive, int visualicacao) {
        this.normaId = normaId;
        this.nome = nome;
        this.descricao = descricao;
        //this.creationUser = creationUser;
        //this.deletionUser = deletionUser;
        this.url = url;
        this.isActive = isActive;
        this.visualizacao = visualizacao;
    }

    public int compareTo(NormaUI norma){
        long id1 = this.normaId;
        long id2 = norma.getNormaId();

        if(id1 > id2){
            return 1;
        }else if(id1 < id2){
            return -1;
        }else{
            return 0;
        }
    }

    public NormaUI() {
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

    public boolean isIsActive() {
        return isActive;
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

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
   
    
}
