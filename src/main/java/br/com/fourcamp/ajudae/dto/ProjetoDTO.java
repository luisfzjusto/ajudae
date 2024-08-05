package br.com.fourcamp.ajudae.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Double meta;
    private Long usuarioId;
    private ContaDTO conta;
}
