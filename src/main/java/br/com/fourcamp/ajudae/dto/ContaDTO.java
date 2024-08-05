package br.com.fourcamp.ajudae.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaDTO {

    private Long id;
    private String numero;
    private Double saldo;
    private Long usuarioId;
    private Long projetoId;
}
