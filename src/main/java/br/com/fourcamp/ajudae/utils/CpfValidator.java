package br.com.fourcamp.ajudae.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<CPF, String> {

    @Override
    public void initialize(CPF cpf){
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")){
            return false;
        }

        int[] pesos = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        if (isCpfValido(cpf, pesos)){
            pesos = new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
            return isCpfValido(cpf, pesos);
        }
        return false;
    }

    private boolean isCpfValido(String cpf, int[] pesos){
        int soma = 0;
        for (int i = 0; i < pesos.length; i++){
            soma += Integer.parseInt(cpf.charAt(i) + "") * pesos[i];
        }
        int resto = 11 - (soma % 11);
        int digito = resto > 9 ? 0 : resto;
        return digito == Integer.parseInt(cpf.charAt(pesos.length) + "");
    }
}

