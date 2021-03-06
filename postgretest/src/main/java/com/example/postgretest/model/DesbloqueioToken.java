package com.example.postgretest.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Temporal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.TemporalType;

import com.example.postgretest.model.Usuario;
import java.util.UUID;
import java.util.Date;

@Entity
public class DesbloqueioToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private long tokenid;

    @Column(name="desbloqueio_token")
    private String desbloqueioToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Usuario user;

    public DesbloqueioToken(){

    }
    
    public DesbloqueioToken(Usuario user) {
        this.user = user;
        createdDate = new Date();
        desbloqueioToken = UUID.randomUUID().toString();
    }

    public String getDesbloqueioToken(){
    	return desbloqueioToken;
    }

    public void setDesbloqueioToken(String token){
    	this.desbloqueioToken = token;
    }

    public Date getCreatedDate(){
    	return createdDate;
    }

    public Usuario getUsuario(){
    	return this.user;
    }
}