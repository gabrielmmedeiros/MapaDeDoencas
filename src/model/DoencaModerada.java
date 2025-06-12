package model;

public class DoencaModerada extends Doenca {
    DoencaModerada(String nome) {
        super(nome);
    }

    @Override
    public String getGrauDeRisco() {
        return "Moderada";
    }
}