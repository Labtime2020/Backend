/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.model;

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
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
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
@Table(name="tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;    
    @Column(length=200)
    @NotBlank
    private String nome;
    
    @ManyToMany(mappedBy="tags")
    private List<Norma> normas = new ArrayList<Norma>();

    public List<Norma> getNormas(){
    	return this.normas;
    }
    
    public Tag(int id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public Tag(){

    }

    public String getNome(){
        return this.nome;
    }
}
