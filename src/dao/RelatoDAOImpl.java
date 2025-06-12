package dao;

import model.Relato;
import model.Usuario;
import model.Doenca;
import model.Local;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatoDAOImpl implements RelatoDAO {
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final DoencaDAO doencaDAO = new DoencaDAOImpl();
    private final LocalDAO localDAO = new LocalDAOImpl();

    @Override
    public Relato criar(Relato relato) {
        String sql = "INSERT INTO relatos (usuario_id, doenca_id, local_id, data) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, relato.getUsuario().getId());
            stmt.setInt(2, relato.getDoenca().getId());
            stmt.setInt(3, relato.getLocal().getId());
            stmt.setTimestamp(4, new Timestamp(relato.getData().getTime()));
            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("Falha ao inserir relato.");
            }
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    // Reconstrói usando DAOs para manter consistência
                    Usuario u = usuarioDAO.buscarPorId(relato.getUsuario().getId());
                    Doenca d = doencaDAO.buscarPorId(relato.getDoenca().getId());
                    Local l = localDAO.buscarPorId(relato.getLocal().getId());
                    return Relato.reconstruir(id, u, d, l, relato.getData());
                }
                throw new RuntimeException("Falha ao obter ID gerado para relato.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar relato.", e);
        }
    }

    @Override
    public Relato buscarPorId(int id) {
        String sql = "SELECT id, usuario_id, doenca_id, local_id, data FROM relatos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = usuarioDAO.buscarPorId(rs.getInt("usuario_id"));
                    Doenca d = doencaDAO.buscarPorId(rs.getInt("doenca_id"));
                    Local l = localDAO.buscarPorId(rs.getInt("local_id"));
                    Timestamp ts = rs.getTimestamp("data");
                    return Relato.reconstruir(rs.getInt("id"), u, d, l, new java.util.Date(ts.getTime()));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar relato por ID.", e);
        }
    }

    @Override
    public List<Relato> listarTodos() {
        String sql = "SELECT id FROM relatos";
        List<Relato> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Relato r = buscarPorId(rs.getInt("id"));
                if (r != null) lista.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar relatos.", e);
        }
        return lista;
    }

    @Override
    public List<Relato> listarPorLocal(int localId) {
        String sql = "SELECT id FROM relatos WHERE local_id = ?";
        List<Relato> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, localId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Relato r = buscarPorId(rs.getInt("id"));
                    if (r != null) lista.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar relatos por local.", e);
        }
        return lista;
    }

    @Override
    public Map<Doenca, Integer> contarPorDoencaPorLocal(int localId) {
        String sql = "SELECT doenca_id, COUNT(*) AS quantidade FROM relatos WHERE local_id = ? GROUP BY doenca_id";
        Map<Doenca, Integer> mapa = new HashMap<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, localId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Doenca d = doencaDAO.buscarPorId(rs.getInt("doenca_id"));
                    mapa.put(d, rs.getInt("quantidade"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar relatos por doença-local.", e);
        }
        return mapa;
    }

    @Override
    public void atualizar(Relato relato) {
        String sql = "UPDATE relatos SET usuario_id = ?, doenca_id = ?, local_id = ?, data = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, relato.getUsuario().getId());
            stmt.setInt(2, relato.getDoenca().getId());
            stmt.setInt(3, relato.getLocal().getId());
            stmt.setTimestamp(4, new Timestamp(relato.getData().getTime()));
            stmt.setInt(5, relato.getId());
            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("Nenhum relato atualizado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar relato.", e);
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM relatos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar relato.", e);
        }
    }
}
