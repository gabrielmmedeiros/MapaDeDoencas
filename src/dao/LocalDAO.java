package dao;

import model.Local;

import java.sql.SQLException;
import java.util.List;

public interface LocalDAO {
    void inserir(Local local) throws SQLException;

    Local buscarPorId(int id) throws SQLException;

    Local buscarPorNome(String nome) throws SQLException;

    List<Local> listarTodos() throws SQLException;

    void deletar(int id) throws SQLException;
}
