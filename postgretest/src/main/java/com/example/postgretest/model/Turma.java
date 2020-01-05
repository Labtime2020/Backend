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
@Table(name = "Turma")
public class Turma implements Serializable {
 
	private static final long serialVersionUID = -2343243243242432341L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
 
	@Column(name = "nometurma")
	private String nomeTurma;
 
	protected Turma() {
	}
 
	public Turma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}
 	
 	public Turma at() {
		return this;
	}

	@Override
	public String toString() {
		return String.format("Turma[id=%d, nome='%s']", id, nomeTurma);
	}
	public String getNomeTurma() {
        return nomeTurma;
    }
    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    @OneToMany(cascade = CascadeType.ALL,
        orphanRemoval = true)
    private List<Curso> cursos = new ArrayList<>();

    public List<Curso> getCursos(){
    	return cursos;
    }
}