package com.example.postgretest.repository;

import java.util.List;

import com.example.postgretest.model.Aluno;

import org.springframework.data.repository.CrudRepository;

public interface AlunoRepository extends CrudRepository<Aluno, Long>{
	
}