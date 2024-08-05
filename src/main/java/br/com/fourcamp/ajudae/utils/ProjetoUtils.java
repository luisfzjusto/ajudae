package br.com.fourcamp.ajudae.utils;

import jakarta.validation.ValidationException;

import java.util.regex.Pattern;

public class ProjetoUtils {

    public static void validarNome(String nome) {
        if (nome == null || nome.length() < 10) {
            throw new ValidationException("O nome do projeto deve possuir, no mínimo, 10 caracteres.");
        }
    }

    public static void validarDescricao(String descricao) {
        if (descricao == null || descricao.length() < 50) {
            throw new ValidationException("A descrição do seu projeto deve possuir, no mínimo, 50 caracteres.");
        }
    }

    public static void validarMeta(Double meta) {
        if (meta == null) {
            throw new ValidationException("A meta do seu projeto não pode ser nula.");
        }
        if (meta <= 0) {
            throw new ValidationException("A meta do seu projeto deve ser um valor positivo.");
        }
    }
}
