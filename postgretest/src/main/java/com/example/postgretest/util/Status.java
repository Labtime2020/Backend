package com.example.postgretest.util;

public final class Status{
	public static final int OK = 0;
	public static final int ERRO = 1;
	public static final int UNADMIN = 2;//administrador unico
	public static final int NONUNADMIN = 3;//administrador nao eh unico
	public static final int ATIVO = 4;//usuario ativo
	public static final int INATIVO = 5;//usuario inativo
	public static final int SENHAERRADA = 6;//tentativa de login com senha errada
	public static final int SEMUSER = 7;//usuario nao existente
        public static final int USERJAEXISTE = 8; //usuario ja existe no sistema
        public static final int BLOQUEADO = 9;
        public static final int NORMAJAEXISTE = 10;//norma ja consta no sistema
        public static final int CAMPOSNULOS = 11;
        public static final int NORMA_INEXISTENTE = 12;
        public static final int MESMASENHA = 13;
        public static final String ME_C_0 = "Norma inexistente";
        public static final String ME01 = "Erro";
        public static final String ME04_2 = "Email ja foi cadastrado por outro usuario";
	public static final String ME09 = "Usuario ou senha incorretos";
	public static final String ME10_2 = "Usuario bloqueado";
	public static final String ME10_1 = "Senha incorreta, usuario bloqueado, instrucoes para desbloqueio enviado no email informado";
	public static final String ME06 = "Usuario inativo";
	public static final String MS02 = "Usuario desbloqueado";
	public static final String ME11 = "Usuario nao se encontra bloqueado";
	public static final String ME12 = "Token invalido";
        public static final String ME15 = "Norma ja cadastrada";
        public static final String ME17 = "Url nulo";
        public static final String ME19 = "A senha fornecida eh a mesma da atual";
        public static final String MS01 = "Operacao realizada com sucesso";
	public static final String DIVISOR = "&&&^^^^^()()()()";
	public static final String API_ADDRESS = "http://localhost:9090";
	public static final String MCREDENTIAL = "LABTIME2020";
	public static final String ERRO_SENHA_ERRADA = "Bad credentials";
	public static final int MAX_NUM_TENTATIVAS = 5;//maximo numero de tentativas erradas no login
}