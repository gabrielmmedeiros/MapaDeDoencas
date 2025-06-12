package dao;

import model.Local;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public interface LocalDAO {

    Local criar(Local local);

    Local buscarPorId(int id);

    Local buscarPorNome(String nome);

    List<Local> listarTodos();

    void atualizar(Local local);

    boolean deletar(int id);
}
