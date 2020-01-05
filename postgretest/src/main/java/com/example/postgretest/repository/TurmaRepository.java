package com.example.postgretest.repository;

import java.util.List;

import com.example.postgretest.model.Turma;

import org.springframework.data.repository.CrudRepository;

public interface TurmaRepository extends CrudRepository<Turma, Long>{
	
}