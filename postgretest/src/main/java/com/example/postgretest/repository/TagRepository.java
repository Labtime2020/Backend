/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.repository;

/**
 *
 * @author labtime
 */
import com.example.postgretest.model.Tag;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {    
	List<Tag> findAll();
	Optional<Tag> findByNome(String nome);
}
