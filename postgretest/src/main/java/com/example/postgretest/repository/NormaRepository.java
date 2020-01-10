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
import java.util.List;

import com.example.postgretest.model.Norma;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface NormaRepository extends CrudRepository<Norma, String> {
    Optional<Norma> findByNome(String nome);
    Optional<Norma> findByNormaId(long normaId);
}
