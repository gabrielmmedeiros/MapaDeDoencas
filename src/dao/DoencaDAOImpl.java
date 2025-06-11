package dao;
import model.Doenca;
import model.Sintoma;
import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC de DoencaDAO.
 * Carregamento de sintomas delegados ao SintomaDAO.
 */
public class DoencaDAOImpl implements DoencaDAO {
    private final SintomaDAO sintomaDAO = new SintomaDAOImpl();

    @Override
    public Doenca criar(Doenca doenca) {
        String sql = "INSERT INTO doencas (nome, gravidade) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, doenca.getNome());
            stmt.setString(2, doenca.getGrauDeRisco());
            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("Falha ao inserir doença.");
            }
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return Doenca.reconstruir(rs.getInt(1), doenca.getNome(), doenca.getGrauDeRisco());
                }
                throw new RuntimeException("Falha ao obter ID gerado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar doença.", e);
        }
    }

    @Override
    public Doenca buscarPorId(int id) {
        String sql = "SELECT id, nome, gravidade FROM doencas WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Doenca d = Doenca.reconstruir(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("gravidade")
                    );
                    List<Sintoma> sintomas = sintomaDAO.buscarPorDoencaId(d.getId());
                    for (Sintoma s : sintomas) {
                        d.adicionarSintoma(s);  // <— chama o método público!
                    }
                    return d;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar doença por ID.", e);
        }
    }

    @Override
    public List<Doenca> listarTodos() {
        String sql = "SELECT id, nome, gravidade FROM doencas";
        List<Doenca> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Doenca d = Doenca.reconstruir(
                        rs.getInt("id"), rs.getString("nome"), rs.getString("gravidade")
                );
                List<Sintoma> sintomas = sintomaDAO.buscarPorDoencaId(d.getId());
                for (Sintoma s : sintomas) {
                    d.adicionarSintoma(s);
                }
                lista.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar doenças.", e);
        }
        return lista;
    }

    @Override
    public List<Doenca> buscarPorGravidade(String gravidade) {
        String sql = "SELECT id, nome, gravidade FROM doencas WHERE gravidade = ?";
        List<Doenca> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gravidade);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Doenca d = Doenca.reconstruir(
                            rs.getInt("id"), rs.getString("nome"), rs.getString("gravidade")
                    );
                    List<Sintoma> sintomas = sintomaDAO.buscarPorDoencaId(d.getId());
                    for (Sintoma s : sintomas) {
                        d.adicionarSintoma(s);
                    }
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por gravidade.", e);
        }
        return lista;
    }

    @Override
    public void atualizar(Doenca doenca) {
        String sql = "UPDATE doencas SET nome = ?, gravidade = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doenca.getNome());
            stmt.setString(2, doenca.getGrauDeRisco());
            stmt.setInt(3, doenca.getId());
            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("Nenhuma doença atualizada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar doença.", e);
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM doencas WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar doença.", e);
        }
    }
}