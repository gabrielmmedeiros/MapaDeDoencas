package dao;

import dao.SintomaDAO;
import model.Sintoma;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC de SintomaDAO.
 */
public class SintomaDAOImpl implements SintomaDAO {

    @Override
    public Sintoma criar(Sintoma sintoma) {
        String sql = "INSERT INTO sintomas (nome) VALUES (?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sintoma.getNome());
            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("Falha ao inserir sintoma.");
            }
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return Sintoma.reconstruir(rs.getInt(1), sintoma.getNome());
                }
                throw new RuntimeException("Falha ao obter ID gerado para sintoma.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar sintoma.", e);
        }
    }

    @Override
    public Sintoma buscarPorId(int id) {
        String sql = "SELECT id, nome FROM sintomas WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Sintoma.reconstruir(rs.getInt("id"), rs.getString("nome"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar sintoma por ID.", e);
        }
    }

    @Override
    public List<Sintoma> listarTodos() {
        String sql = "SELECT id, nome FROM sintomas";
        List<Sintoma> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(Sintoma.reconstruir(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar sintomas.", e);
        }
        return lista;
    }

    @Override
    public List<Sintoma> buscarPorDoencaId(int doencaId) {
        String sql = "SELECT s.id, s.nome FROM sintomas s " +
                "JOIN doenca_sintoma ds ON s.id = ds.sintoma_id " +
                "WHERE ds.doenca_id = ?";
        List<Sintoma> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doencaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(Sintoma.reconstruir(rs.getInt("id"), rs.getString("nome")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar sintomas por doença.", e);
        }
        return lista;
    }

    @Override
    public void atualizar(Sintoma sintoma) {
        String sql = "UPDATE sintomas SET nome = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sintoma.getNome());
            stmt.setInt(2, sintoma.getId());
            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("Nenhum sintoma atualizado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar sintoma.", e);
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM sintomas WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar sintoma.", e);
        }
    }

    @Override
    public void associarDoenca(int sintomaId, int doencaId) {
        String sql = "INSERT INTO doenca_sintoma (doenca_id, sintoma_id) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doencaId);
            stmt.setInt(2, sintomaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao associar sintoma à doença.", e);
        }
    }

    @Override
    public void desassociarDoenca(int sintomaId, int doencaId) {
        String sql = "DELETE FROM doenca_sintoma WHERE doenca_id = ? AND sintoma_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doencaId);
            stmt.setInt(2, sintomaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover associação sintoma-doença.", e);
        }
    }
}

