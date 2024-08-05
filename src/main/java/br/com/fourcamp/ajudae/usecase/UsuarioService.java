package br.com.fourcamp.ajudae.usecase;

import br.com.fourcamp.ajudae.dao.ContaDAO;
import br.com.fourcamp.ajudae.dao.ProjetoDAO;
import br.com.fourcamp.ajudae.dao.UsuarioDAO;
import br.com.fourcamp.ajudae.dto.ContaDTO;
import br.com.fourcamp.ajudae.dto.UsuarioDTO;
import br.com.fourcamp.ajudae.exception.InvalidRequestException;
import br.com.fourcamp.ajudae.model.Conta;
import br.com.fourcamp.ajudae.model.Projeto;
import br.com.fourcamp.ajudae.model.Usuario;
import br.com.fourcamp.ajudae.utils.UsuarioUtils;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UsuarioService {
    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private ContaDAO contaDAO;

    @Transactional
    public UsuarioDTO criarUsuarioComConta(UsuarioDTO usuarioDTO){

        if(usuarioDTO.getNome() == null || usuarioDTO.getNome().isEmpty() || usuarioDTO.getCpf() == null || usuarioDTO.getCpf().isEmpty() || usuarioDTO.getDataNascimento() == null || usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isEmpty()){
            throw new InvalidRequestException();
        }

        try {
            UsuarioUtils.validarNome(usuarioDTO.getNome());
            UsuarioUtils.validarCPF(usuarioDTO.getCpf());
            UsuarioUtils.validarEmail(usuarioDTO.getEmail());
            UsuarioUtils.validarMaiorIdade(usuarioDTO.getDataNascimento());
        } catch (ValidationException e){
            throw new ValidationException(e.getMessage());
        }

        Conta conta = new Conta();
        conta.setNumero(UUID.randomUUID().toString());
        conta.setSaldo(0.0);
        conta.setUsuarioId(null);
        conta.setProjetoId(null);
        int contaId = contaDAO.salvar(conta);
        conta.setId((long) contaId);

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setDataNascimento(usuarioDTO.getDataNascimento());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setConta(conta);
        int usuarioId = usuarioDAO.salvar(usuario);
        usuario.setId((long) usuarioId);

        conta.setUsuarioId((long)usuarioId);
        contaDAO.atualizar(conta);

        usuarioDTO.setId((long) usuarioId);
        usuarioDTO.setConta(new ContaDTO(conta.getId(), conta.getNumero(), conta.getSaldo(), conta.getUsuarioId(), conta.getProjetoId()));
        return usuarioDTO;
    }

    public List<UsuarioDTO> listarUsuarios(){
        List<Usuario> usuarios = usuarioDAO.listar();
        return usuarios.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Double buscarSaldoPorId(Long id){
        return usuarioDAO.buscarSaldoPorId(id);
    }

    @Transactional
    public void deletarUsuarioPorId(Long id){
        Double saldo = usuarioDAO.buscarSaldoPorId(id);
        int projetos = usuarioDAO.contarProjetosPorUsuarioId(id);

        if(projetos > 0){
            throw new RuntimeException("Não é possível excluir um usuário com projetos ativos.");
        }
        if(saldo > 0){
            throw new RuntimeException("Não é possível excluir um usuário com saldo em conta.");
        }

        usuarioDAO.removerReferenciaConta(id);

        contaDAO.deletarPorUsuarioId(id);

        usuarioDAO.deletarPorId(id);
    }

    private UsuarioDTO convertToDTO(Usuario usuario){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setCpf(usuario.getCpf());
        usuarioDTO.setDataNascimento(usuario.getDataNascimento());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setConta(new ContaDTO(usuario.getConta().getId(), usuario.getConta().getNumero(), usuario.getConta().getSaldo(), usuario.getConta().getUsuarioId(), usuario.getConta().getProjetoId()));
        return usuarioDTO;
    }
}
