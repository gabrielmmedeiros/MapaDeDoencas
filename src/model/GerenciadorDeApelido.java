package model;

import java.util.*;
import model.Usuario;
import dao.UsuarioDAO;

/**
 * Gerencia a criação de usuários com apelidos únicos, antes de persistir o dado.
 * - A chave é o apelido.
 * - O valor é o objeto Usuario.
 */
public class GerenciadorDeApelido {
    private static final int MAX_TENTATIVAS = 1600;
    // Mapa de apelidos para objetos Usuario (garante unicidade e acesso rápido)
    private final Map<String, Usuario> usuariosPorApelido = new HashMap<>();

    /**
     * Cria um novo Usuario com apelido único.
     * Tenta gerar apelidos até encontrar um que ainda não exista no Map.
     * Retorna novo Usuario criado com apelido exclusivo
     */
    public Usuario criarUsuarioUnico() {
        int tentativas = 0;
        Usuario novo;
        do {
            if(++tentativas>MAX_TENTATIVAS){
                throw new IllegalStateException("Não foi possível gerar um apelido único em " + MAX_TENTATIVAS + " tentativas");
            }
            novo = Usuario.criar();
        } while (usuariosPorApelido.containsKey(novo.getApelido()));

        usuariosPorApelido.put(novo.getApelido(), novo);
        return novo;
    }

    /**
     * Retorna todos os usuários já criados.
     *
     * Retorna lista com os objetos Usuario
     */
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuariosPorApelido.values());
    }

    /**
     * Verifica se um apelido já foi usado.
     *
     * Recebe como parâmetro uma String com o apelido a verificar
     * Retorna true se já existe, false caso contrário
     */
    public boolean apelidoJaExiste(String apelido) {
        return usuariosPorApelido.containsKey(apelido);
    }

    /**
     * Retorna o usuário com determinado apelido.
     *
     * Este metodo recebe um parâmetro chamado apelido, que representa o apelido que será usado na busca
     * Retorna objeto Usuario, ou null se não existir
     */
    public Usuario buscarUsuarioPorApelido(String apelido) {
        return usuariosPorApelido.get(apelido);
    }

    /**
     * Remove um usuário pelo apelido, se existir.
     *
     * O metodo recebe um parâmetro chamado apelido, que representa o apelido do usuário que será removido.
     * Retorna true se foi removido, false se não existia
     */
    public boolean removerUsuario(String apelido) {
        Usuario removido = usuariosPorApelido.remove(apelido);
        return removido != null;
    }


    //Retorna o total de usuários registrados.
    public int quantidade() {
        return usuariosPorApelido.size();
    }
}
