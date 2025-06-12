package dao;

import model.Sintoma;
import java.util.List;

/**
 * Interface DAO para operações CRUD de Sintoma e associação de Doenca.
 */
public interface SintomaDAO {

    Sintoma criar(Sintoma sintoma);

    Sintoma buscarPorId(int id);

    List<Sintoma> listarTodos();

    List<Sintoma> buscarPorDoencaId(int doencaId);

    void atualizar(Sintoma sintoma);

    boolean deletar(int id);

    void associarDoenca(int sintomaId, int doencaId);

    void desassociarDoenca(int sintomaId, int doencaId);
}