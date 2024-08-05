package br.com.fourcamp.ajudae.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaqueDTO {
    private Long contaId;
    private Double valor;
}
