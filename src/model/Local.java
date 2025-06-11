package model;

/**
 * Representa uma localidade onde uma doença foi contraída.
 */

public class Local implements IdentificavelPorNome {
    private int id; // sera preenchido pelo banco via DAO
    private final String nome;

    public Local(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do local não pode ser vazio.");
        }
        this.nome = nome.trim();
    }
    //Para o DAO conseguir puxar o local criado para o banco
    public static Local reconstruir(int id, String nome) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        Local l = new Local(nome);
        l.id = id; // acesso direto permitido dentro da própria classe
        return l;
    }


    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }



    public String toString() {
        return nome;
    }
}
