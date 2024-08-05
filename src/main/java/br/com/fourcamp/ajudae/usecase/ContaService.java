package br.com.fourcamp.ajudae.usecase;

import br.com.fourcamp.ajudae.dao.ContaDAO;
import br.com.fourcamp.ajudae.model.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContaService {

    @Autowired
    private ContaDAO contaDAO;

    @Transactional
    public void depositar(Long contaId, Double valor) {
        Conta conta = contaDAO.buscarPorId(contaId);
        if (conta == null){
            throw new RuntimeException("Conta não encontrada.");
        }
        contaDAO.depositar(contaId, valor);
    }

    @Transactional
    public void sacar(Long contaId, Double valor) {
        Conta conta = contaDAO.buscarPorId(contaId);
        if (conta == null){
            throw new RuntimeException("Conta não encontrada.");
        }
        contaDAO.sacar(contaId, valor);
    }

    @Transactional
    public void apoiarProjeto(Long usuarioId, Long projetoId, Double valor) {
        contaDAO.apoiarProjeto(usuarioId, projetoId, valor);
    }
}
