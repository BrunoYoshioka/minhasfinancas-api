package com.gft.brunoyoshioka.minhasFinancas.exception;

public class ErroAutentication extends RuntimeException {
    public ErroAutentication(String mensagem){
        super(mensagem);
    }
}
