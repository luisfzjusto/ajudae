package br.com.fourcamp.ajudae.dao;

import br.com.fourcamp.ajudae.model.Projeto;
import br.com.fourcamp.ajudae.model.Conta;
import br.com.fourcamp.ajudae.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProjetoDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ContaDAO contaDao;

    @Autowired
    private UsuarioDAO usuarioDAO;

    private RowMapper<Projeto> rowMapper = new RowMapper<Projeto>(){
        @Override
        public Projeto mapRow(ResultSet rs, int rowNum) throws SQLException{
            Projeto projeto = new Projeto();
            projeto.setId(rs.getLong("id"));
            projeto.setNome(rs.getString("nome"));
            projeto.setDescricao(rs.getString("descricao"));
            projeto.setMeta(rs.getDouble("meta"));
            projeto.setUsuario(usuarioDAO.buscarPorId(rs.getLong("usuario_id")));
            projeto.setConta(contaDao.buscarPorId(rs.getLong("conta_id")));
            return projeto;
        }
    };

    public int salvar(Projeto projeto) {
        String sql = "INSERT INTO projeto (nome, descricao, meta, usuario_id, conta_id) VALUES (?, ?, ?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, new Object[]{projeto.getNome(), projeto.getDescricao(), projeto.getMeta(), projeto.getUsuario().getId(), projeto.getConta().getId()}, Integer.class);
    }

    public List<Projeto> listar() {
        String sql = "SELECT * FROM projeto";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Projeto buscarPorId(Long id) {
        String sql = "SELECT * FROM projeto WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    }

    public void atualizar(Projeto projeto){
        String sql = "UPDATE projeto SET meta = ? WHERE id = ?";
        jdbcTemplate.update(sql, projeto.getMeta(), projeto.getId());
    }

    public void deletarPorID(Long id){
        Projeto projeto = buscarPorId(id);
        if(projeto != null){
            contaDao.deletarPorProjetoId(projeto.getConta().getId());
            String sql = "DELETE FROM projeto WHERE id = ?";
            jdbcTemplate.update(sql, id);
        }
    }
}
