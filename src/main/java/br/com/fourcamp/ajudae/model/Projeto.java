package br.com.fourcamp.ajudae.model;

import lombok.Data;

@Data
public class Projeto {

    private Long id;
    private String nome;
    private String descricao;
    private Double meta;
    private Usuario usuario;
    private Conta conta;
}
