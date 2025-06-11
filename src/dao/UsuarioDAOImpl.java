package dao;

import model.Usuario;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC de UsuarioDAO.
 */
public class UsuarioDAOImpl implements UsuarioDAO {
    @Override
    public void criar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (apelido) VALUES (?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getApelido());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Falha ao inserir usuário, nenhuma linha afetada.");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.atribuirId(keys.getInt(1));
                } else {
                    throw new RuntimeException("Falha ao obter ID do usuário inserido.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário no banco de dados.", e);
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT id, apelido FROM usuarios WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Usuario.reconstruir(rs.getInt("id"), rs.getString("apelido"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID.", e);
        }
    }

    @Override
    public Usuario buscarPorApelido(String apelido) {
        String sql = "SELECT id, apelido FROM usuarios WHERE apelido = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, apelido);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Usuario.reconstruir(rs.getInt("id"), rs.getString("apelido"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por apelido.", e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT id, apelido FROM usuarios";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(Usuario.reconstruir(rs.getInt("id"), rs.getString("apelido")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários.", e);
        }
        return lista;
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET apelido = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getApelido());
            stmt.setInt(2, usuario.getId());
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Nenhum usuário foi atualizado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário.", e);
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário.", e);
        }
    }
}