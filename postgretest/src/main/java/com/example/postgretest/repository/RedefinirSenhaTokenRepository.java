package com.example.postgretest.repository;

import java.util.List;
import java.util.Optional;

import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.RedefinirSenhaToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface RedefinirSenhaTokenRepository extends CrudRepository<RedefinirSenhaToken, String> {
    RedefinirSenhaToken findByRedefinirSenhaToken(String redefinirSenhaToken);

    Optional<RedefinirSenhaToken> findByUserId(long userId);
}