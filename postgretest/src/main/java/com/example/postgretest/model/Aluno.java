package com.example.postgretest.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@Entity
@Table(name = "Aluno")
public class Aluno implements Serializable {
 
	private static final long serialVersionUID = -2343243243242432341L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
 
	@Column(name = "nomealuno")
	private String nomeAluno;
 
	protected Aluno() {
	}
 
	public Aluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}
 	
 	public Aluno at() {
		return this;
	}

	@Override
	public String toString() {
		return String.format("Turma[id=%d, nome='%s']", id, nomeAluno);
	}

	public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }
}