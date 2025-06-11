package model;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Objects;

public abstract class Doenca implements IdentificavelPorNome {
    private final String nome;
    private int id;
    private final Set<Sintoma> sintomas = new HashSet<>(); // Mantém o Set

    protected Doenca(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da doença não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    // Métodos fábrica (criarNovaDoenca, reconstruir) e atribuirId - permanecem os mesmos.
    public static Doenca criarNovaDoenca(String nome, String gravidadeConhecida) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da doença para criarNovaDoenca não pode ser vazio.");
        }
        if (gravidadeConhecida == null || gravidadeConhecida.trim().isEmpty()) {
            throw new IllegalArgumentException("A gravidade para criarNovaDoenca não pode ser vazia.");
        }

        return switch (gravidadeConhecida.toUpperCase()) {
            case "LEVE" -> new DoencaLeve(nome);
            case "MODERADA" -> new DoencaModerada(nome);
            case "GRAVE" -> new DoencaGrave(nome);
            default -> throw new IllegalArgumentException("Gravidade inválida para criarNovaDoenca: " + gravidadeConhecida);
        };
    }

    public static Doenca reconstruir(int id, String nome, String gravidade) {
        Doenca d;
        switch (gravidade.toUpperCase()) {
            case "LEVE" -> d = new DoencaLeve(nome);
            case "MODERADA" -> d = new DoencaModerada(nome);
            case "GRAVE" -> d = new DoencaGrave(nome);
            default -> throw new IllegalArgumentException("Gravidade inválida para reconstruir: " + gravidade);
        }
        d.atribuirId(id);
        return d;
    }

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


    //para uso interno (pelas subclasses e pelo DAO)
    void adicionarSintomaInternamente(Sintoma sintoma) {
        Objects.requireNonNull(sintoma, "Sintoma não pode ser nulo para adicionar internamente à Doença.");
        if (this.sintomas.add(sintoma)) {
            // Assumindo que Sintoma.java tem o metodo "adicionarDoencaInternamente(Doenca d)" package-private
            sintoma.adicionarDoencaInternamente(this); // Mantém a bidirecionalidade
        }
    }
    public void adicionarSintoma(Sintoma sintoma) {
        adicionarSintomaInternamente(sintoma);
    }

    public Set<Sintoma> getSintomas() {
        return Collections.unmodifiableSet(sintomas);
    }

    @Override
    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public abstract String getGrauDeRisco();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doenca other)) return false;
        return this.nome.equals(other.nome);
    }

    @Override
    public int hashCode() {
        return this.nome.hashCode();
    }
}