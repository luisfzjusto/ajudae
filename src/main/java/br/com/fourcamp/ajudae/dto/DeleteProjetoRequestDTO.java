package br.com.fourcamp.ajudae.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProjetoRequestDTO {
    private Long usuarioId;
    private Long projetoId;
}
