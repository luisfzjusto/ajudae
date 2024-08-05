package br.com.fourcamp.ajudae.dao;

import br.com.fourcamp.ajudae.model.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ContaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Conta> rowMapper = (rs, rowNum) -> {
        Conta conta = new Conta();
        conta.setId(rs.getLong("id"));
        conta.setNumero(rs.getString("numero"));
        conta.setSaldo(rs.getDouble("saldo"));
        conta.setUsuarioId(rs.getLong("usuario_id"));
        conta.setProjetoId(rs.getLong("projeto_id"));
        return conta;
    };

    public int salvar(Conta conta){
        String sql = "INSERT INTO conta (numero, saldo, usuario_id, projeto_id) VALUES (?, ?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, new Object[]{conta.getNumero(), conta.getSaldo(), conta.getUsuarioId(), conta.getProjetoId()}, Integer.class);
    }

    public int atualizar(Conta conta) {
        String sql = "UPDATE conta SET saldo = ?, usuario_id = ?, projeto_id = ? WHERE id = ?";
        return jdbcTemplate.update(sql, conta.getSaldo(), conta.getUsuarioId(), conta.getProjetoId(), conta.getId());
    }

    public Conta buscarPorId(Long id) {
        String sql = "SELECT * FROM conta WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    }

    public void deletarPorUsuarioId(Long usuarioId){
        String sql = "DELETE FROM conta WHERE usuario_id = ?";
        jdbcTemplate.update(sql, usuarioId);
    }

    public void deletarPorProjetoId(Long projetoId){
        String sql = "DELETE FROM conta WHERE projeto_id = ?";
        jdbcTemplate.update(sql, projetoId);
    }

    public Conta buscarPorUsuarioId(Long usuarioId){
        String sql = "SELECT * FROM conta WHERE usuario_id = ?";
        List<Conta> contas = jdbcTemplate.query(sql, new Object[]{usuarioId}, rowMapper);
        if (contas.isEmpty()) {
            return null;
        } else if (contas.size() > 1) {
            throw new IllegalStateException("Encontrada mais de uma conta para um usuário");
        }
        return contas.get(0);
    }

    public void deletarPorId(Long id){
        String sql = "DELETE FROM conta WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void depositar(Long contaId, Double valor){
        String sql = "UPDATE conta SET saldo = saldo + ? WHERE id = ?";
        jdbcTemplate.update(sql, valor, contaId);
    }

    public void sacar(Long contaId, Double valor){
        String sql = "UPDATE conta SET saldo = saldo - ? WHERE id = ?";
        jdbcTemplate.update(sql, valor, contaId);
    }

    @Transactional
    public void apoiarProjeto(Long contaUsuarioId, Long contaProjetoId, Double valor){

        sacar(contaUsuarioId, valor);

        depositar(contaProjetoId, valor);
    }

    @Transactional
    public void transferir(Long contaOrigemId, Long contaDestinoId, Double valor){

        sacar(contaOrigemId, valor);

        depositar(contaDestinoId, valor);
    }
}
