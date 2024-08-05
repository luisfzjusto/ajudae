package br.com.fourcamp.ajudae.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(){
        super("Formato de requisição inválido");
    }
}
