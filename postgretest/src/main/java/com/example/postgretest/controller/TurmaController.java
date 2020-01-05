package com.example.postgretest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.postgretest.model.Turma;
import com.example.postgretest.model.Curso;
import com.example.postgretest.repository.TurmaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class TurmaController {
	@Autowired
	TurmaRepository repository;
	
	@GetMapping("/bulkcreateturma")
	public String bulkcreate(){
		Turma turma = new Turma("2016/1");

		turma.getCursos().add(
			new Curso("TC", "2019/1")
		);

		turma.getCursos().add(
			new Curso("Compiladores", "2019/1")
		);
		// save a single Customer
		repository.save(turma);
		
		return "Turma criada";
	}

	@GetMapping("/delete/{id}")
	public String delete_turma(@PathVariable long id){
		repository.delete(repository.findById(id).get());

		return "Turma deletada!";
	}
}