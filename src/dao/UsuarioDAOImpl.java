package dao;

import model.Usuario;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final Connection conn;

    public UsuarioDAOImpl() {
        this.conn = ConnectionFactory.getConnection();
    }


    public Usuario inserir(Usuario usuario) {
        String sql = "INSERT INTO Usuario (apelido) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getApelido());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                return Usuario.reconstruir(idGerado, usuario.getApelido());
            } else {
                throw new SQLException("Falha ao obter ID gerado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário: " + e.getMessage(), e);
        }
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM Usuario WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Usuario.reconstruir(rs.getInt("id"), rs.getString("apelido"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }
        return null;
    }

    public Usuario buscarPorApelido(String apelido) {
        String sql = "SELECT * FROM Usuario WHERE apelido = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, apelido);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Usuario.reconstruir(rs.getInt("id"), rs.getString("apelido"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por apelido: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(Usuario.reconstruir(rs.getInt("id"), rs.getString("apelido")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
        return usuarios;
    }

    public void atualizar(Usuario usuario) {
        String sql = "UPDATE Usuario SET apelido = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getApelido());
            stmt.setInt(2, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM Usuario WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage(), e);
        }
    }
}
