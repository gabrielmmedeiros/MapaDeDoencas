package dao;

import model.Doenca;
import model.Sintoma;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Interface DAO para operações CRUD de Doenca.
 */
public interface DoencaDAO {
    Doenca criar(Doenca doenca);
    Doenca buscarPorId(int id);
    List<Doenca> listarTodos();
    List<Doenca> buscarPorGravidade(String gravidade);
    void atualizar(Doenca doenca);
    boolean deletar(int id);
}