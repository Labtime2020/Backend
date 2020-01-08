package com.example.postgretest.util;

public final class Status{
	public static final int OK = 0;
	public static final int ERRO = 1;
	public static final int UNADMIN = 2;//administrador unico
	public static final int NONUNADMIN = 3;//administrador nao eh unico
	public static final int ATIVO = 4;//usuario ativo
	public static final int INATIVO = 5;//usuario inativo
}