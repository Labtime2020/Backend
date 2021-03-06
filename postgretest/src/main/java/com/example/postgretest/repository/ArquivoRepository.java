/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.repository;

import com.example.postgretest.model.Arquivo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author labtime
 */

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long>{
    @Query(value="select * from arquivo where fk_normaid=?1", nativeQuery=true)
    Optional<Arquivo> findByNorma(Long normaId);
    
}