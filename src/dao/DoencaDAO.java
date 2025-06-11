package dao;

import model.Doenca;

import java.sql.SQLException;
import java.util.List;

public interface DoencaDAO {
    void inserir(Doenca doenca) throws SQLException;

    Doenca buscarPorId(int id) throws SQLException;

    Doenca buscarPorNome(String nome) throws SQLException;

    List<Doenca> listarTodas() throws SQLException;

    void deletar(int id) throws SQLException;
}
