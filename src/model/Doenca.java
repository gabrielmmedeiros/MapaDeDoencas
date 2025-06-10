package model;
/**
 * Representa uma doença informada por um usuário.
 * O grau de risco é definido pelas subclasses.
 */

public abstract class Doenca {
    private final String nome;
    private int id;// sera preenchido pelo banco via DAO

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
