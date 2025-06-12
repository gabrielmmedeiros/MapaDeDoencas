package model;

public class DoencaGrave extends Doenca {
    DoencaGrave(String nome) {
        super(nome);
    }

    @Override
    public String getGrauDeRisco() {
        return "Grave";
    }
}