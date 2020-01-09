/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

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
    
    /*@OneToOne
    @JoinColumn(name="email")
    private Usuario deletionUser;*/
    
    public Norma(long normaId, String nome, String descricao, String url, Date registerDate, Date deletionDate, Usuario creationUser, Usuario deletionUser) {
        this.normaId = normaId;
        this.nome = nome;
        this.descricao = descricao;
        this.url = url;
        this.registerDate = registerDate;
        this.deletionDate = deletionDate;
        this.creationUser = creationUser;
        this.deletionUser = deletionUser;
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
    
    
}