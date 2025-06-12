package dao;

import model.Relato;

import model.Doenca;
import java.util.List;
import java.util.Map;

/**
 * Interface DAO para operações CRUD e agregação de Relato.
 */
public interface RelatoDAO {

    Relato criar(Relato relato);

    Relato buscarPorId(int id);

    List<Relato> listarTodos();

    List<Relato> listarPorLocal(int localId);

    Map<Doenca, Integer> contarPorDoencaPorLocal(int localId);

    void atualizar(Relato relato);

    boolean deletar(int id);
}