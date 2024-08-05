package br.com.fourcamp.ajudae.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class Conta {

    private Long id;
    private String numero;
    private Double saldo;
    private Long usuarioId;
    private Long projetoId;
}
