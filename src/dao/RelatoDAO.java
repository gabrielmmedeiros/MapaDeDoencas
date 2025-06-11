package dao;

import model.Doenca;
import model.Local;
import model.Relato;
import model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface RelatoDAO {
    Relato inserir(Relato relato) throws SQLException;

    boolean existeRelatoDoUsuario(Usuario usuario, Doenca doenca, Local local) throws SQLException;

    int contarRelatosPorDoencaELocal(Doenca doenca, Local local) throws SQLException;

    List<Relato> listarTodos() throws SQLException;

    Relato buscarPorId(int id) throws SQLException;

    void deletar(int id) throws SQLException;
}
