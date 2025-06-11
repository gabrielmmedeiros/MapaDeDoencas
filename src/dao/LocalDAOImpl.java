package dao;

import model.Local;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDAOImpl implements LocalDAO {

    private final Connection conn;

    public LocalDAOImpl() {
        this.conn = ConnectionFactory.getConnection();
    }


    public void inserir(Local local) throws SQLException {
        String sql = "INSERT INTO local (nome) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, local.getNome());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    // reconstrução com o ID atribuído pelo banco
                    Local reconstruido = Local.reconstruir(rs.getInt(1), local.getNome());
                    // ou, se preferir atualizar o objeto existente, pode expor atribuirId
                }
            }
        }
    }

    public Local buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM local WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Local.reconstruir(rs.getInt("id"), rs.getString("nome"));
            }
        }
        return null;
    }

    public Local buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM local WHERE nome = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Local.reconstruir(rs.getInt("id"), rs.getString("nome"));
            }
        }
        return null;
    }

    public List<Local> listarTodos() throws SQLException {
        List<Local> locais = new ArrayList<>();
        String sql = "SELECT * FROM local";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                locais.add(Local.reconstruir(rs.getInt("id"), rs.getString("nome")));
            }
        }

        return locais;
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM local WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
