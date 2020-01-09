package com.example.postgretest.repository;

import com.example.postgretest.model.DesbloqueioToken;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface DesbloqueioTokenRepository extends CrudRepository<DesbloqueioToken, String> {
    DesbloqueioToken findByDesbloqueioToken(String desbloqueioToken);
}