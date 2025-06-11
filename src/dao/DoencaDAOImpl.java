package dao;

import model.*;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoencaDAOImpl implements DoencaDAO {
    private final Connection conn;

    public DoencaDAOImpl(Connection conn) {
        this.conn = conn;
    }


    public void inserir(Doenca doenca) throws SQLException {
        String sql = "INSERT INTO doenca (nome, gravidade) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, doenca.getNome());
            stmt.setString(2, doenca.getGrauDeRisco().toUpperCase()); // LEVE, MODERADA, GRAVE
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Doenca.reconstruir(rs.getInt(1), doenca.getNome(), doenca.getGrauDeRisco()); // opcional, útil se você quiser reconstruir depois
            }
        }
    }

    public Doenca buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM doenca WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Doenca.reconstruir(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("gravidade")
                );
            }
        }
        return null;
    }


    public Doenca buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM doenca WHERE nome = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Doenca.reconstruir(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("gravidade")
                );
            }
        }
        return null;
    }


    public List<Doenca> listarTodas() throws SQLException {
        List<Doenca> doencas = new ArrayList<>();
        String sql = "SELECT * FROM doenca";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Doenca d = Doenca.reconstruir(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("gravidade")
                );
                doencas.add(d);
            }
        }
        return doencas;
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM doenca WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
