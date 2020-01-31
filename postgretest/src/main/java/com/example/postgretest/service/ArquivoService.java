/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.service;

import com.example.postgretest.model.Arquivo;
import com.example.postgretest.repository.ArquivoRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author labtime
 */
@Service
public class ArquivoService {
    @Autowired
    ArquivoRepository arquivoRepository;
    
    @Autowired
    AutowireCapableBeanFactory autowirer;
    
    public boolean temArquivo(long normaId){
        Optional<Arquivo> arqChk = arquivoRepository.findByNorma(normaId);
        return ( arqChk.isEmpty() == true ) ? false :  true;
    }
}
