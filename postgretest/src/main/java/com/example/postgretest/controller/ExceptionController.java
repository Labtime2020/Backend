/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.postgretest.controller;


import com.example.postgretest.exception.FileIntegrityException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author labtime
 */

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    String handleFileException(HttpServletRequest request, Throwable ex) {
        //return your json insted this string.
        System.out.println("Problema no upload do arquivo!!");
        return "Falha ao processar";
    }
    @ExceptionHandler(FileIntegrityException.class)
    @ResponseBody
    String handleFileIntegrityException(Throwable ex){
        System.out.println("Integridade do arquivo mudada!");
        return "Falha ao mandar";
    }
}