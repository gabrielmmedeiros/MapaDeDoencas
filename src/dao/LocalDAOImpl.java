package dao;

import model.Local;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDAOImpl implements LocalDAO {

    @Override
    public Local criar(Local local) {
        String sql = "INSERT INTO locais (nome) VALUES (?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, local.getNome());
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Falha ao inserir local.");
            }
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return Local.reconstruir(rs.getInt(1), local.getNome());
                }
                throw new RuntimeException("Falha ao obter ID gerado para local.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar local.", e);
        }
    }

    @Override
    public Local buscarPorId(int id) {
        String sql = "SELECT id, nome FROM locais WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Local.reconstruir(rs.getInt("id"), rs.getString("nome"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar local por ID.", e);
        }
    }

    @Override
    public Local buscarPorNome(String nome) {
        String sql = "SELECT id, nome FROM locais WHERE nome = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Local.reconstruir(rs.getInt("id"), rs.getString("nome"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar local por nome.", e);
        }
    }

    @Override
    public List<Local> listarTodos() {
        String sql = "SELECT id, nome FROM locais";
        List<Local> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(Local.reconstruir(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar locais.", e);
        }
        return lista;
    }

    @Override
    public void atualizar(Local local) {
        String sql = "UPDATE locais SET nome = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, local.getNome());
            stmt.setInt(2, local.getId());
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Nenhum local atualizado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar local.", e);
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM locais WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar local.", e);
        }
    }
}
