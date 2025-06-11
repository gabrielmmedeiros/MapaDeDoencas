package dao;

import model.Usuario;
import java.util.List;

public interface UsuarioDAO {
    Usuario inserir(Usuario usuario);
    Usuario buscarPorId(int id);
    Usuario buscarPorApelido(String apelido);
    List<Usuario> listarTodos();
    void atualizar(Usuario usuario);
    void deletar(int id);
}

