package br.com.fourcamp.ajudae.model;


import lombok.Data;
import java.util.Date;
import java.util.Set;

@Data
public class Usuario {

    private Long id;
    private String nome;
    private String cpf;
    private Date dataNascimento;
    private String email;
    private Conta conta;
    private Set<Projeto> projetos;
}
