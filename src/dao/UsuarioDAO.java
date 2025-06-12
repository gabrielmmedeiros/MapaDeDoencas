package dao;

import model.Usuario;
import java.util.List;

/**
 * Interface DAO para operações CRUD de Usuario.
 * Metodo criar agora retorna o objeto Usuario reconstruído com ID.
 */
public interface UsuarioDAO {
    Usuario criar(Usuario usuario);
    Usuario buscarPorId(int id);
    Usuario buscarPorApelido(String apelido);
    List<Usuario> listarTodos();
    void atualizar(Usuario usuario);
    boolean deletar(int id);
}

