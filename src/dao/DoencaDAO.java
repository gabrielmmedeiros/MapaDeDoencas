package dao;

import model.Doenca;
import java.util.List;

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