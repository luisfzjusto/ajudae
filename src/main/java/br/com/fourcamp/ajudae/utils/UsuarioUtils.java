package br.com.fourcamp.ajudae.utils;

import jakarta.validation.ValidationException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.regex.Pattern;

public class UsuarioUtils {

    public static void validarNome(String nome){
        if (!Pattern.matches("^[a-zA-Z\\s]{10,}$", nome)){
            throw new ValidationException("O nome deve conter apenas letras e deve possuir, no mínimo, 10 caracteres.");
        }
    }

    public static void validarCPF(String cpf){
        if (!Pattern.matches("\\d{11}", cpf)){
            throw new ValidationException("O CPF deve conter 11 dígitos.");
        }
    }

    public static void validarEmail(String email){
        if (!Pattern.matches("^[^@]+@[^@]+\\.[^@]{2,}$", email)){
            throw new ValidationException("O e-mail deve possuir um domínio válido.");
        }
    }

    public static void validarMaiorIdade(Date dataNascimento){
        if (!isMaiorDeIdade(dataNascimento)){
            throw new ValidationException("Cadastro não permitido para menores de idade.");
        }
    }

    public static boolean isMaiorDeIdade(Date dataNascimento){
        LocalDate nascimento = new java.sql.Date(dataNascimento.getTime()).toLocalDate();
        return Period.between(nascimento, LocalDate.now()).getYears() >= 18;
    }
}
