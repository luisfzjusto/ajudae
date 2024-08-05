package br.com.fourcamp.ajudae.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApoiarProjetoDTO {
    private Long usuarioId;
    private Long projetoId;
    private Double valor;
}
