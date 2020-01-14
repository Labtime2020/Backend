/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.model;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author labtime
 */
public class NormaUI {
    public long normaId;
    public String nome;
    public String descricao;
    //public long creationUser;//id do usuario que criou a norma
    //public long deletionUser;//id do usuario que removeu a norma
    public String url;
    public boolean isActive;
    public List< String > tags = new ArrayList<>();

    public NormaUI(long normaId, String nome, String descricao, String url, boolean isActive) {
        this.normaId = normaId;
        this.nome = nome;
        this.descricao = descricao;
        //this.creationUser = creationUser;
        //this.deletionUser = deletionUser;
        this.url = url;
        this.isActive = isActive;
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
