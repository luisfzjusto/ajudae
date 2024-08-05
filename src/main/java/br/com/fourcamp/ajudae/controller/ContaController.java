package br.com.fourcamp.ajudae.controller;

import br.com.fourcamp.ajudae.dto.SaqueDTO;
import br.com.fourcamp.ajudae.dto.ApoiarProjetoDTO;
import br.com.fourcamp.ajudae.dto.DepositoDTO;
import br.com.fourcamp.ajudae.usecase.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping("/depositar")
    public ResponseEntity<String> depositar(@RequestBody DepositoDTO depositoDTO ){
        try{
            contaService.depositar(depositoDTO.getContaId(), depositoDTO.getValor());
            return new ResponseEntity<>("Depósito realizado com sucesso!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sacar")
    public ResponseEntity<String> sacar(@RequestBody SaqueDTO saqueDTO){
        try{
            contaService.sacar(saqueDTO.getContaId(), saqueDTO.getValor());
            return new ResponseEntity<>("Saque realizado com sucesso!", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/apoiar")
    public ResponseEntity<String> apoiarProjeto(@RequestBody ApoiarProjetoDTO apoiarProjetoDTO){
        try{
            contaService.apoiarProjeto(apoiarProjetoDTO.getUsuarioId(), apoiarProjetoDTO.getProjetoId(), apoiarProjetoDTO.getValor());
            return new ResponseEntity<>("Obrigado por apoiar esse projeto!", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
