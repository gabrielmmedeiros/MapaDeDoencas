package model;
import java.util.Objects;

/**
 * Representa uma localidade onde uma doença foi contraída.
 */

public class Local implements IdentificavelPorNome {
    private int id; // sera preenchido pelo banco via DAO
    private final String nome;

    // Construtor privado
    private Local(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do local não pode ser vazio.");
        }
        this.nome = nome.trim();
    }
    // Metodo fábrica para criar um novo Local (quando ainda não tem ID)
    public static Local criarNovoLocal(String nome) {
        return new Local(nome);
    }
    void atribuirId(int id) { // package-private
        if (this.id > 0 && this.id!= id) { // Se já tem um ID positivo e o novo ID é diferente
            throw new IllegalStateException("Este objeto Local já possui um ID (" + this.id + ") e não pode ser alterado para um ID diferente (" + id + ").");
        }
        if (this.id == 0 && id <= 0) { // Se ainda não tem ID (this.id == 0), o novo ID deve ser positivo
            throw new IllegalArgumentException("ID inválido (" + id + ") para um novo objeto Local que ainda não possui ID.");
        }
        if (this.id > 0 && id <= 0) { // Não pode invalidar um ID existente
            throw new IllegalArgumentException("Não é possível atribuir um ID inválido (" + id + ") a um objeto Local que já possui um ID válido (" + this.id + ").");
        }
        this.id = id;
    }
    //Para o DAO conseguir puxar o local criado para o banco
    public static Local reconstruir(int id, String nome) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        Local l = new Local(nome);
        l.atribuirId(id);
        return l;
    }



    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Local that)) return false;
        return Objects.equals(this.nome, that.nome);
    }

    public int hashCode() {
        return Objects.hash(nome);
    }



    public String toString() {
        return nome;
    }
}
