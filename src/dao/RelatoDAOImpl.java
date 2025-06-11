package dao;

import model.*;

import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatoDAOImpl implements RelatoDAO {
    private final Connection conn;

    public RelatoDAOImpl() {
        this.conn = ConnectionFactory.getConnection();
    }

    public Relato inserir(Relato relato) throws SQLException {
        if (existeRelatoDoUsuario(relato.getUsuario(), relato.getDoenca(), relato.getLocal())) {
            throw new IllegalStateException("Este usuário já relatou essa doença neste local.");
        }

        String sql = "INSERT INTO relato (usuario_id, doenca_id, local_id) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, relato.getUsuario().getId());
            stmt.setInt(2, relato.getDoenca().getId());
            stmt.setInt(3, relato.getLocal().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return Relato.reconstruir(
                            rs.getInt(1),
                            relato.getUsuario(),
                            relato.getDoenca(),
                            relato.getLocal()
                    );
                } else {
                    throw new SQLException("Falha ao obter ID do relato inserido.");
                }
            }
        }
    }

    public boolean existeRelatoDoUsuario(Usuario usuario, Doenca doenca, Local local) throws SQLException {
        String sql = "SELECT 1 FROM relato WHERE usuario_id = ? AND doenca_id = ? AND local_id = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getId());
            stmt.setInt(2, doenca.getId());
            stmt.setInt(3, local.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true se existe pelo menos um
            }
        }
    }

    public int contarRelatosPorDoencaELocal(Doenca doenca, Local local) throws SQLException {
        String sql = "SELECT COUNT(*) FROM relato WHERE doenca_id = ? AND local_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doenca.getId());
            stmt.setInt(2, local.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Relato> listarTodos() throws SQLException {
        List<Relato> relatos = new ArrayList<>();
        String sql = "SELECT * FROM relato";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
                DoencaDAO doencaDAO = new DoencaDAOImpl(conn);
                LocalDAO localDAO = new LocalDAOImpl();

                Usuario usuario = usuarioDAO.buscarPorId(rs.getInt("usuario_id"));
                Doenca doenca = doencaDAO.buscarPorId(rs.getInt("doenca_id"));
                Local local = localDAO.buscarPorId(rs.getInt("local_id"));

                Relato r = Relato.reconstruir(rs.getInt("id"), usuario, doenca, local);
                relatos.add(r);
            }
        }

        return relatos;
    }

    public Relato buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM relato WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
                    DoencaDAO doencaDAO = new DoencaDAOImpl(conn);
                    LocalDAO localDAO = new LocalDAOImpl();

                    Usuario usuario = usuarioDAO.buscarPorId(rs.getInt("usuario_id"));
                    Doenca doenca = doencaDAO.buscarPorId(rs.getInt("doenca_id"));
                    Local local = localDAO.buscarPorId(rs.getInt("local_id"));

                    return Relato.reconstruir(id, usuario, doenca, local);
                }
            }
        }

        return null;
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM relato WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
