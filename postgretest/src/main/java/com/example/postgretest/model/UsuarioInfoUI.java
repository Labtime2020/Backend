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
public class UsuarioInfoUI {
    public Long id;
    public String email;
    public String nome;
    public String sobrenome;
    public boolean isAdmin;
    public int status;

    public UsuarioInfoUI(){
        
    }
        
    public UsuarioInfoUI(Long id, String email, String nome, String sobrenome, boolean isAdmin, int status) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.isAdmin = isAdmin;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public int getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
