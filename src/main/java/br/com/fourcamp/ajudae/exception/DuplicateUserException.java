package br.com.fourcamp.ajudae.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(){
        super("Usuário já cadastrado.");
    }
}
