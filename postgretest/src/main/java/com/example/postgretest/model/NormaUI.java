/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.model;

/**
 *
 * @author labtime
 */
public class NormaUI {
    public long normaId;
    public String nome;
    public String descricao;
    public long creationUser;//id do usuario que criou a norma
    public long deletionUser;//id do usuario que removeu a norma
    public String url;
    public boolean isActive;

    public NormaUI(long normaId, String nome, String descricao, long creationUser, long deletionUser, String url, boolean isActive) {
        this.normaId = normaId;
        this.nome = nome;
        this.descricao = descricao;
        this.creationUser = creationUser;
        this.deletionUser = deletionUser;
        this.url = url;
        this.isActive = isActive;
    }

    public long getUserId() {
        return normaId;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public long getCreationUser() {
        return creationUser;
    }

    public long getDeletionUser() {
        return deletionUser;
    }
    public String getUrl() {
        return url;
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

    public void setCreationUser(long creationUser) {
        this.creationUser = creationUser;
    }

    public void setDeletionUser(long deletionUser) {
        this.deletionUser = deletionUser;
    }


    public void setUrl(String url) {
        this.url = url;
    }
    
   
    
}
