package model;

public class DoencaLeve extends Doenca {
    // Construtor package-private simples
    DoencaLeve(String nome) {
        super(nome);
    }

    @Override
    public String getGrauDeRisco() {
        return "Leve";
    }
}
