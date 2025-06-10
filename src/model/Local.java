package model;

/**
 * Representa uma localidade onde uma doença foi contraída.
 */

public class Local {
    private int id; // sera preenchido pelo banco via DAO
    private final String nome;

    public Local(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do local não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    // usado pelo DAO após inserir no banco e obter o ID gerado
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        this.id = id;
    }


    public String toString() {
        return nome;
    }
}
