/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author labtime
 */
@Entity
@Table(name="arquivo")
public class Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long fid;
    private String filename;
    
    @OneToOne(optional=true)
    //@JoinTable(name="norma_id", joinColumns = {@JoinColumn(name="fk_fid")},
      //          inverseJoinColumns = { @JoinColumn(name = "fk_norma", unique = true) })
    @JoinColumn(name="fk_normaid")
    private Norma norma;

    public Arquivo() {
    }

    public Arquivo(long fid, String filename, Norma norma) {
        this.fid = fid;
        this.filename = filename;
        this.norma = norma;
    }

    public long getFid() {
        return fid;
    }

    public String getFilename() {
        return filename;
    }

    public Norma getNorma() {
        return norma;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setNorma(Norma norma) {
        this.norma = norma;
    }
    
    
    
}
