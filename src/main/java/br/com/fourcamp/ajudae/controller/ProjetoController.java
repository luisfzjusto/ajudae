package br.com.fourcamp.ajudae.controller;

import br.com.fourcamp.ajudae.dto.ApoiarProjetoDTO;
import br.com.fourcamp.ajudae.dto.DeleteProjetoRequestDTO;
import br.com.fourcamp.ajudae.dto.ProjetoDTO;
import br.com.fourcamp.ajudae.model.Projeto;
import br.com.fourcamp.ajudae.usecase.ProjetoService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<String> criarProjeto(@Valid @RequestBody ProjetoDTO projetoDTO){
        try{
            ProjetoDTO novoProjeto = projetoService.criarProjeto(projetoDTO);
            return new ResponseEntity<>("Projeto cadastrado com sucesso!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjetoDTO>> listarProjetos(){
        List<ProjetoDTO> projetos = projetoService.listarProjetos();
        return new ResponseEntity<>(projetos, HttpStatus.OK);
    }

    @PostMapping("/apoiar")
    public ResponseEntity<String> apoiarProjeto(@RequestBody ApoiarProjetoDTO request){
        try{
            projetoService.apoiarProjeto(request.getUsuarioId(), request.getProjetoId(), request.getValor());
            return new ResponseEntity<>("Obrigado por apoiar esse projeto!", HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<String> deletarProjeto(@RequestBody DeleteProjetoRequestDTO deleteProjetoRequest){
        try{
            projetoService.deletarProjeto(deleteProjetoRequest.getProjetoId());
            return new ResponseEntity<>("Projeto excluído com sucesso!", HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
