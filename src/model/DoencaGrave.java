package model;

public class DoencaGrave extends Doenca {
    // Construtor package-private simples
    DoencaGrave(String nome) {
        super(nome);
    }

    @Override
    public String getGrauDeRisco() {
        return "Grave";
    }
}