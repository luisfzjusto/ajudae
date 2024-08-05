package br.com.fourcamp.ajudae.controller;

import br.com.fourcamp.ajudae.dto.DeleteUsuarioRequestDTO;
import br.com.fourcamp.ajudae.dto.SaldoRequestDTO;
import br.com.fourcamp.ajudae.dto.UsuarioDTO;
import br.com.fourcamp.ajudae.exception.InvalidRequestException;
import br.com.fourcamp.ajudae.usecase.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        try {
            UsuarioDTO novoUsuario = usuarioService.criarUsuarioComConta(usuarioDTO);
            return new ResponseEntity<>("Usuário cadastrado com sucesso!", HttpStatus.CREATED);
        } catch (InvalidRequestException | ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saldo")
    public ResponseEntity<Double> buscarSaldo(@RequestBody SaldoRequestDTO saldoRequest){
        try{
            Double saldo = usuarioService.buscarSaldoPorId(saldoRequest.getUsuarioId());
            return new ResponseEntity<>(saldo, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<String> deletarUsuario(@RequestBody DeleteUsuarioRequestDTO deleteUsuarioRequest){
        try{
            usuarioService.deletarUsuarioPorId(deleteUsuarioRequest.getUsuarioId());
            return new ResponseEntity<>("Usuário excluído com sucesso!", HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios(){
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

}
