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
public class UsuarioUI extends UsuarioInfoUI {
    public String password;

    public UsuarioUI(){

    }
    
    public UsuarioUI( Long id, String email, String nome, String sobrenome, boolean isAdmin, String password, int status) {
        super(id, email, nome, sobrenome, isAdmin, status);
        this.password = password;
        
    }

    

    public String getPassword() {
        return password;
    }

    

    public void setPassword(String password) {
        this.password = password;
    }

    
    
    
}
