package model;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class Sintoma implements IdentificavelPorNome {
    private int id; // Gerado pelo banco
    private final String nome;
    private final Set<Doenca> doencasAssociadas = new HashSet<>();

    // Construtor privado
    private Sintoma(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do sintoma não pode ser vazio.");
        }
        this.nome = nome.trim().toLowerCase(); // Padronizar para minúsculas para evitar duplicatas por case
    }

    // Metodo fábrica para criar um novo Sintoma (sem ID)
    public static Sintoma criarNovoSintoma(String nome) {
        return new Sintoma(nome);
    }

    // Metodo fábrica para reconstruir do banco (com ID)
    public static Sintoma reconstruir(int id, String nome) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido para reconstrução do Sintoma.");
        }
        Sintoma s = new Sintoma(nome);
        s.id = id;
        return s;
    }

    // Metodo package-private para o DAO (e o metodo reconstruir) setar o ID.
    void atribuirId(int id) {
        if (this.id > 0 && this.id!= id) { // Permite reatribuir o mesmo ID, mas não um diferente se já tiver um > 0
            throw new IllegalStateException("Este objeto já possui um ID atribuído anteriormente e diferente do fornecido.");
        }
        if (this.id == 0 && id <= 0) { // Se ainda não tem ID, o novo ID deve ser positivo
            throw new IllegalArgumentException("ID inválido para um novo objeto.");
        }
        if (id <= 0 && this.id > 0) { // Não pode setar para um ID inválido se já tinha um válido
            throw new IllegalArgumentException("Não é possível atribuir um ID inválido a um objeto que já possui ID.");
        }
        this.id = id;
    }

    @Override
    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sintoma sintoma)) return false;
        // Compara pelo nome (já padronizado para minúsculas no construtor)
        return nome.equals(sintoma.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return "Sintoma{id=" + id + ", nome='" + nome + '\'' + '}';
    }

    public Set<Doenca> getDoencasAssociadas() {
        return Collections.unmodifiableSet(doencasAssociadas);
    }


    void adicionarDoenca(Doenca doenca) {
        this.doencasAssociadas.add(doenca);
    }
    void adicionarDoencaInternamente(Doenca doenca) {
        Objects.requireNonNull(doenca, "Doença não pode ser nula para adicionar internamente ao Sintoma.");
        this.doencasAssociadas.add(doenca);
    }
    void removerDoencaInternamente(Doenca doenca) {
        Objects.requireNonNull(doenca, "Doença não pode ser nula para remover internamente do Sintoma.");
        this.doencasAssociadas.remove(doenca);
    }

    void removerDoenca(Doenca doenca) {
        this.doencasAssociadas.remove(doenca);
    }
}