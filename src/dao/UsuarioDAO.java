package dao;

import model.Usuario;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface DAO para operações CRUD de Usuario.
 */
public interface UsuarioDAO {
    void criar(Usuario usuario);
    Usuario buscarPorId(int id);
    Usuario buscarPorApelido(String apelido);
    List<Usuario> listarTodos();
    void atualizar(Usuario usuario);
    boolean deletar(int id);
}