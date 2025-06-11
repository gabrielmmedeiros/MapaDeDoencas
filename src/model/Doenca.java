package model;

/**
 * Representa uma doença informada por um usuário.
 * O grau de risco é definido pelas subclasses.
 */

public abstract class Doenca implements IdentificavelPorNome {
    private final String nome;
    private int id;// sera preenchido pelo banco via DAO

    public int getId() {
        return id;
    }
    // Metodo protegido usado pelas subclasses em reconstruções
    void atribuirId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        this.id = id;
    }

    public static Doenca reconstruir(int id, String nome, String gravidade) {
        Doenca d;
        switch (gravidade.toUpperCase()) {
            case "LEVE" -> d = new DoencaLeve(nome);
            case "MODERADA" -> d = new DoencaModerada(nome);
            case "GRAVE" -> d = new DoencaGrave(nome);
            default -> throw new IllegalArgumentException("Gravidade inválida: " + gravidade);
        }
        d.atribuirId(id); // metodo private que seta o ID via reflection ou dentro da própria classe
        return d;
    }



    protected Doenca(String nome) {//protected (impede classes externas de criar doença diretamente), sem afetar as subclasses
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da doença não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getNome() {
        return nome;
    }
    public abstract String getGrauDeRisco();//metodo abstrato
}
