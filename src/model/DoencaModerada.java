package model;

public class DoencaModerada extends Doenca {
    public DoencaModerada(String nome) {
        super(nome);
    }

    public String getGrauDeRisco() {
        return "Moderada";
    }
}
