package com.gft.brunoyoshioka.minhasFinancas.exception;
// classe customizada que será lançada toda vez que tiver uma exceção
public class RegraNegocioException extends RuntimeException {
    // construtor para receber mensagem como parametro
    public RegraNegocioException(String msg){
        // chamar contrutor da classe super passando a msg
        super(msg);
    }
}
