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
    
}
