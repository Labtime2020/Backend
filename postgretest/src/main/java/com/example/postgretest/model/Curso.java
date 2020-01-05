package com.example.postgretest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Curso")
public class Curso implements Serializable {
 
	private static final long serialVersionUID = -2343243243242432341L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
 
	@Column(name = "nomecurso")
	private String nomeCurso;
 
	@Column(name = "datainicio")
	private String dataInicio;
 
	protected Curso() {
	}
 
	public Curso(String nomeCurso, String dataInicio) {
		this.nomeCurso = nomeCurso;
		this.dataInicio = dataInicio;
	}
 	
 	public Curso at() {
		return this;
	}

	@Override
	public String toString() {
		return String.format("Curso[id=%d, nome='%s', inicio='%s']", id, nomeCurso, dataInicio);
	}
	public String getNomeCurso() {
        return nomeCurso;
    }
    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }
    public String getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }
}