package br.com.fourcamp.ajudae.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String cpf;
    private Date dataNascimento;
    private String email;
    private ContaDTO conta;
}
