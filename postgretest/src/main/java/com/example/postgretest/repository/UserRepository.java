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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.postgretest.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
	List<Usuario> findByEmail(String email);
        
	List<Usuario> findAllByOrderByNome();
	List<Usuario> findByIsAdmin(boolean isAdmin);

	List<Usuario> findByEmailContaining(String email);
}
