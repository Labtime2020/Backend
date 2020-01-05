package com.example.postgretest.controller;

import com.example.postgretest.model.Aluno;
import com.example.postgretest.repository.AlunoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class AlunoController {
	@Autowired
	AlunoRepository repository;
	
	@GetMapping("/bulkcreatealuno")
	public String bulkcreate(){
		Aluno aluno = new Aluno("Rafaelcs");
		// save a single Customer
		repository.save(aluno);
		
		return "Aluno criado";
	}
}