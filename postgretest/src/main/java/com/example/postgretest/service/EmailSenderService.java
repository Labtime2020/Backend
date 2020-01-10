package com.example.postgretest.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import com.example.postgretest.model.Usuario;
import com.example.postgretest.model.DesbloqueioToken;
import com.example.postgretest.repository.DesbloqueioTokenRepository;

@Service("emailSenderService")
public class EmailSenderService {
	@Autowired
    private DesbloqueioTokenRepository desbloqueioTokenRepository;

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    @Async
    public void sendDesbloqueioToken(Usuario user){
    	DesbloqueioToken token = new DesbloqueioToken(user);

    	desbloqueioTokenRepository.save(token);

    	SimpleMailMessage msg = new SimpleMailMessage();

    	msg.setTo(user.getEmail());

    	msg.setSubject("Desbloqueio de usuario");

    	msg.setText("Para desbloquear seu usuario acesse o link http://localhost:9090/desbloquear?token=" + token.getDesbloqueioToken());

    	this.sendEmail(msg);
    }
    @Async
    public void sendEmailComModificacoes(Usuario user, String msg){
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(user.getEmail());
        mensagem.setSubject("Alteracoes na norma");
        mensagem.setText(msg);
        
        this.sendEmail(mensagem);
    }
}