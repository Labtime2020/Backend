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
public class UsuarioUI {
    public long id;
    public String email;
    public String nome;
    public String sobrenome;
    public String password;
    public boolean isAdmin;

    public UsuarioUI(String email, String nome, String sobrenome, boolean isAdmin, String password) {
        this.email = email;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.isAdmin = isAdmin;
        this.password = password;
    }

    public long getId() {
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

    public String getPassword() {
        return password;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setId(long id) {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    
}