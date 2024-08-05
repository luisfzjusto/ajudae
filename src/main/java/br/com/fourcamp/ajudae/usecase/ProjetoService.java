package br.com.fourcamp.ajudae.usecase;

import br.com.fourcamp.ajudae.dao.ContaDAO;
import br.com.fourcamp.ajudae.dao.ProjetoDAO;
import br.com.fourcamp.ajudae.dao.UsuarioDAO;
import br.com.fourcamp.ajudae.dto.ContaDTO;
import br.com.fourcamp.ajudae.dto.ProjetoDTO;
import br.com.fourcamp.ajudae.model.Conta;
import br.com.fourcamp.ajudae.model.Projeto;
import br.com.fourcamp.ajudae.model.Usuario;
import br.com.fourcamp.ajudae.utils.ProjetoUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProjetoService {

    @Autowired
    private ProjetoDAO projetoDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private ContaDAO contaDAO;

    @Transactional
    public ProjetoDTO criarProjeto(ProjetoDTO projetoDTO){

        ProjetoUtils.validarNome(projetoDTO.getNome());
        ProjetoUtils.validarDescricao(projetoDTO.getDescricao());
        ProjetoUtils.validarMeta(projetoDTO.getMeta());

        Usuario usuario = usuarioDAO.buscarPorId(projetoDTO.getUsuarioId());
        if (usuario == null){
            throw new RuntimeException("Usuário não encontrado");
        }

        Conta conta = new Conta();
        conta.setNumero(UUID.randomUUID().toString());
        conta.setSaldo(0.0);
        conta.setUsuarioId(projetoDTO.getUsuarioId());
        conta.setProjetoId(null);
        int contaId = contaDAO.salvar(conta);
        conta.setId((long) contaId);

        Projeto projeto = new Projeto();
        projeto.setNome(projetoDTO.getNome());
        projeto.setDescricao(projetoDTO.getDescricao());
        projeto.setMeta(projetoDTO.getMeta());
        projeto.setUsuario(usuario);
        projeto.setConta(conta);
        int projetoId = projetoDAO.salvar(projeto);
        projeto.setId((long) projetoId);

        conta.setProjetoId((long) projetoId);
        contaDAO.atualizar(conta);

        projetoDTO.setId((long) projetoId);
        projetoDTO.setConta(new ContaDTO(conta.getId(), conta.getNumero(), conta.getSaldo(), conta.getUsuarioId(), conta.getProjetoId()));
        return projetoDTO;
    }

    public List<ProjetoDTO> listarProjetos(){
        List<Projeto> projetos = projetoDAO.listar();
        return projetos.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void deletarProjeto(Long id){
        Projeto projeto = projetoDAO.buscarPorId(id);
        if(projeto.getMeta() > 0){
            throw new RuntimeException("Não é possível excluir o projeto antes de atingir a meta estabelecida.");
        }
        projetoDAO.deletarPorID(id);
    }

    private ProjetoDTO convertToDTO(Projeto projeto){
        ProjetoDTO projetoDTO = new ProjetoDTO();
        projetoDTO.setId(projeto.getId());
        projetoDTO.setNome(projeto.getNome());
        projetoDTO.setDescricao(projeto.getDescricao());
        projetoDTO.setMeta(projeto.getMeta());
        projetoDTO.setUsuarioId(projeto.getUsuario().getId());
        projetoDTO.setConta(new ContaDTO(projeto.getConta().getId(), projeto.getConta().getNumero(), projeto.getConta().getSaldo(), projeto.getConta().getUsuarioId(), projeto.getConta().getProjetoId()));
        return projetoDTO;
    }

    @Transactional
    public void apoiarProjeto(Long usuarioId, Long projetoId, Double valor){
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        Projeto projeto = projetoDAO.buscarPorId(projetoId);

        if(usuario == null){
            throw new RuntimeException("Usuário não encontrado.");
        }
        if(projeto == null){
            throw new RuntimeException("Projeto não encontrado");
        }

        Conta contaUsuario = usuario.getConta();
        Conta contaProjeto = projeto.getConta();
        Conta contaCriador = projeto.getUsuario().getConta();

        if(contaUsuario.getSaldo() < valor){
            throw new RuntimeException("Saldo insuficiente para apoiar esse projeto. Consulte seu saldo.");
        }

        contaDAO.apoiarProjeto(contaUsuario.getId(), contaProjeto.getId(), valor);

        contaDAO.transferir(contaProjeto.getId(), contaCriador.getId(), valor);

        projeto.setMeta(projeto.getMeta() - valor);

        projetoDAO.atualizar(projeto);

        if(projeto.getMeta() <= 0){
            projetoDAO.deletarPorID(projetoId);
        }
    }
}
