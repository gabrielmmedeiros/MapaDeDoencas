package model;

public class DoencaModerada extends Doenca {
    // Construtor package-private simples
    DoencaModerada(String nome) {
        super(nome);
    }

    @Override
    public String getGrauDeRisco() {
        return "Moderada";
    }
}