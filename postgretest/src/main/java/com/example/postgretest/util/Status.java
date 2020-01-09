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
	public static final String ME09 = "Usuario nao cadastrado";
	public static final String ME10_2 = "Senha incorreta, tentativa de erro contabilizada";
	public static final String ME06 = "Usuario inativo";
}