package br.com.fourcamp.ajudae.dao;

import br.com.fourcamp.ajudae.exception.DuplicateUserException;
import br.com.fourcamp.ajudae.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class UsuarioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ContaDAO contaDAO;

    private RowMapper<Usuario> rowMapper = new RowMapper<Usuario>() {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("id"));
            usuario.setNome(rs.getString("nome"));
            usuario.setCpf(rs.getString("cpf"));
            usuario.setDataNascimento(rs.getDate("data_nascimento"));
            usuario.setEmail(rs.getString("email"));
            usuario.setConta(contaDAO.buscarPorId(rs.getLong("conta_id")));
            return usuario;
        }
    };

    public int salvar(Usuario usuario){
        String sql = "INSERT INTO usuario (nome, cpf, data_nascimento, email, conta_id) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{usuario.getNome(), usuario.getCpf(), usuario.getDataNascimento(), usuario.getEmail(), usuario.getConta().getId()}, Integer.class);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateUserException();
        } catch (Exception e){
            throw e;
        }
    }

    public List<Usuario> listar(){
        String sql = "SELECT * FROM usuario";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Usuario buscarPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        List<Usuario> usuarios = jdbcTemplate.query(sql, new Object[]{id}, rowMapper);
        if (usuarios.isEmpty()) {
            return null;
        } else if (usuarios.size() > 1) {
            throw new IllegalStateException("Encontrado mais de um usuário com o mesmo Id.");
        }
        return usuarios.get(0);
    }

    public Double buscarSaldoPorId(Long id){
        String sql = "SELECT saldo FROM conta WHERE usuario_id = ?";
        List<Double> saldos = jdbcTemplate.queryForList(sql, new Object[]{id}, Double.class);
        if (saldos.isEmpty()) {
            return null;
        } else if (saldos.size() > 1) {
            throw new IllegalStateException("Encontrada mais de uma conta associada a um id de usuário");
        }
        return saldos.get(0);
    }

    public int contarProjetosPorUsuarioId(Long id){
        String sql = "SELECT COUNT(*) FROM projeto WHERE usuario_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
    }

    public void deletarPorId(Long id){
        String sql = "DELETE FROM usuario WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void removerReferenciaConta(Long id) {
        String sql = "UPDATE usuario SET conta_id = NULL WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
