/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.model;

import java.io.Serializable;

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
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author labtime
 */
@Entity
@Table(name="usuario", uniqueConstraints = @UniqueConstraint(columnNames = "email", name="emailConstraint"))
public class Usuario implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;
    @Column(length=200)
    @NotBlank
    private String nome;
    @Column(length=100)
    @NotBlank
    private String avatar;
    @Column(length=100)
    @NotBlank
    private String email;
    @Column(length=200)
    @NotBlank
    private String sobrenome;
    @NotBlank
    @Size(max=15)
    private String password;
    private int status;//situacao do usuario
    private boolean isAdmin;
    private int tentativaErrada;
    private boolean online;
    @Column
    @Temporal(TemporalType.DATE)
    private Date registerDate;
    @Column
    @Temporal(TemporalType.DATE)
    private Date adminBeginDate;
    @Column
    @Temporal(TemporalType.DATE)
    private Date adminEndDate;
    @Column
    @Temporal(TemporalType.DATE)
    private Date lastInteractionDate;

    @ManyToMany
    @JoinTable(name = "usuario_norma",
            joinColumns = { @JoinColumn(name = "fk_usuario")},
            inverseJoinColumns = { @JoinColumn(name = "fk_norma") })
    private List<Norma> favoritos = new ArrayList<Norma>();//usuarios que favoritaram essa norma

    public List<Norma> getFavoritos(){
        return this.favoritos;
    }

    public Usuario(){
    }

    public Usuario(long id, String nome, String email, String sobrenome, String password, boolean isAdmin, int status) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.sobrenome = sobrenome;
        this.password = password;
        this.status = 1;
        this.isAdmin = isAdmin;
        this.tentativaErrada = 0;
        this.lastInteractionDate = new Date();
    }
    
    public UsuarioUI toUsuarioUI(){
        return new UsuarioUI(this.getId(), this.getEmail(), this.getNome(), this.getSobrenome(),
                this.getIsAdmin(), this.getPassword(), this.getStatus());
    }   

    public void atualizarEntrada(){
        this.setLastInteractionDate(new Date());
        this.setOnline(true);
    }

    public void setLastInteractionDate(Date date){
        this.lastInteractionDate = date;
    }

    public Date getLastInteractionDate(){
        return this.lastInteractionDate;
    }

    public boolean getOnline(){
        return this.online;
    }

    public void setOnline(boolean online){
        this.online = online;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public String getAvatar(){
        return this.avatar;
    }

    public int getStatus() {
        return status;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public Date getAdminBeginDate() {
        return adminBeginDate;
    }

    public Date getAdminEndDate() {
        return adminEndDate;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public void setAdminBeginDate(Date adminBeginDate) {
        this.adminBeginDate = adminBeginDate;
    }

    public void setAdminEndDate(Date adminEndDate) {
        this.adminEndDate = adminEndDate;
    }
    
    public Usuario at() {
        return this;
    }

    public int getTentativaErrada(){
        return this.tentativaErrada;
    }

    public void addTentativaErrada(){
        this.tentativaErrada++;
    }

    public void zerarTentativaErrada(){
        this.tentativaErrada = 0;
    }
}
