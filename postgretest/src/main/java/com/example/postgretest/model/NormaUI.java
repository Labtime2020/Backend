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
    private long userId;
    private String nome;
    private String descricao;
    private long creationUser;//id do usuario que criou a norma
    private long deletionUser;//id do usuario que removeu a norma

    public NormaUI(long userId, String nome, String descricao, long creationUser, long deletionUser) {
        this.userId = userId;
        this.nome = nome;
        this.descricao = descricao;
        this.creationUser = creationUser;
        this.deletionUser = deletionUser;
    }

    public long getUserId() {
        return userId;
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

    public void setUserId(long userId) {
        this.userId = userId;
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
    
   
    
}
